package com.randomstatistic.weka

import weka.core._
import scala.io.Source
import WekaConversions._
import java.io.File
import java.util

class WekaDataFormat(name: String, classifications: WekaNominalAttributeType, attributes: List[WekaAttributeType]) {

  val allAttributes = classifications :: attributes
  lazy val attrNameMap = allAttributes.map((a) => (a.name, a)).toMap

  def newInstances: Instances = {
    val attrList = new util.ArrayList[Attribute](attributes.length + 1)
    for (a <- allAttributes)
      attrList.add(a)
    val instances = new Instances(name, attrList, 100) // initial size (100) doesn't really matter
    instances.setClassIndex(0) //first attribute is always the one with the classifications
    instances
  }

  def generateInstance(attrs: Map[String, String]): Instance = {
    val instance = new SparseInstance(allAttributes.length)
    for ((k, v) <- attrs) {
      if (attrNameMap.contains(k))
        instance.setValue(attrNameMap(k), v)
      else
        throw new NoSuchElementException("key " + k + " does not exist in the DataFormat")
    }
    instance
  }

  def nominalPositionName(name: String, value: Double): String = {
    // rgh, has to be a better way to get WekaAttribute inheritance-like behavior without
    // giving up on them being case classes
    val attr = attrNameMap(name) match {
      case b: WekaBooleanAttribute => b
      case n: WekaNominalAttribute => n
      case _ => throw new IllegalArgumentException(name + " is not a nominal attribute")
    }
    attr.values(value.toInt)
  }

  def classificationName(value: Double): String = {
    nominalPositionName(classifications.name, value)
  }

  def classificationDistributionMap(a: Array[Double]): Map[String, Double] = {
    val distribution = for (i <- 0 until a.length) yield {
      (classificationName(i), a(i))
    }
    distribution.toMap
  }
}

class WekaDataSet(dataFormat: WekaDataFormat) {
  val instances = dataFormat.newInstances
  var delinator: String = "\t"
  var normalizer: (String) => String = (s: String) => s.trim
  var valueGenerator: (Header, Array[String]) => Map[String, String] = (h, l) => Map()

  def loadFromCSVWithHeader(filename: String): WekaDataSet = loadFromCSVWithHeader(new File(filename))

  def loadFromCSVWithHeader(file: File): WekaDataSet = {
    val source = Source.fromFile(file).getLines()
    val header = new Header(source.next().split(delinator).toList.map(normalizer))

    loadFromCSV(source, header)
  }

  def loadFromCSV(source: Iterator[String], header: Header): WekaDataSet = {
    for (line <- source) {
      instances.add(generateInstance(header, line.split(delinator).map(normalizer)))
    }
    this // makes for easier implicit conversion one-liners
  }

  def generateInstance(header: Header, line: Array[String]): Instance = {
    val fileValues = for (col <- header.name2Col.keys if dataFormat.attrNameMap.contains(col)) yield {
      (col, line(header.name2Col(col)))
    }
    val generatedValues = for ((name, value) <- valueGenerator(header, line)) yield {
      if (!dataFormat.attrNameMap.contains(name))
        throw new NoSuchElementException("valueGenerator generated something not in the dataset")
      (name, value)
    }
    val allValues = Map() ++ fileValues ++ generatedValues
    val instance = dataFormat.generateInstance(allValues)
    instance
  }
}

class Header(header: List[String]) {
  lazy val name2Col = header.view.zipWithIndex.toMap
  lazy val col2Name = name2Col.map((p) => (p._2, p._1))
}

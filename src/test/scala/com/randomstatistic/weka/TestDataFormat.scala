package com.randomstatistic.weka

import org.scalatest.FunSuite
import weka.core.Instances


class TestDataFormat extends FunSuite {

  def getDataFormat: WekaDataFormat = {
    new WekaDataFormat("df", new WekaBooleanAttribute("desired"), List(
      WekaBooleanAttribute("good"),
      WekaBooleanAttribute("meh"),
      WekaNominalAttribute("likes chili", List("always", "sometimes", "never", "allergic"))
    ))
  }

  val df = getDataFormat

  test("attributes list constructed properly") {
    assert(df.allAttributes.head.name === "desired") // classification attribute is always first
    assert(df.allAttributes.length === 4)
  }

  test("attrNameMap got set up") {
    val expected = List("desired", "good", "likes chili", "meh").sorted
    assert(df.attrNameMap.keys.toList.sorted === expected)
  }

  test("Instances generation") {
    val i1 = df.newInstances
    val i2 = df.newInstances
    assert(i1.isInstanceOf[Instances])
    assert(i2.isInstanceOf[Instances])
    assert(!(i1 eq i2))
  }

}

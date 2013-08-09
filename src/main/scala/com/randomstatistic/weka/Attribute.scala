package com.randomstatistic.weka

import weka.core.Attribute
import scala.collection.JavaConverters._


trait WekaAttributeType {
  val name: String
  val asAttribute: Attribute
}

trait WekaNominalAttributeType extends WekaAttributeType {
  val values: List[String]
}

case class WekaNominalAttribute(name: String, values: List[String]) extends WekaNominalAttributeType {
  lazy val asAttribute = new Attribute(name, values.asJava)
}

case class WekaBooleanAttribute(name: String) extends WekaNominalAttributeType {
  val values = List("true", "false")
  lazy val asAttribute = new Attribute(name, values.asJava)
}

case class WekaNumericAttribute(name: String) extends WekaAttributeType {
  lazy val asAttribute = new Attribute(name)
}

case class WekaDateAttribute(name: String, dateFormat: String) extends WekaAttributeType {
  lazy val asAttribute = new Attribute(name, dateFormat)
}

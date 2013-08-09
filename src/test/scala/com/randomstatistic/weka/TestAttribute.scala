package com.randomstatistic.weka

import org.scalatest.FunSuite

class TestAttribute extends FunSuite {

  test("quasi-inheritance") {
    assert(WekaNominalAttribute("a", List("a", "b")).isInstanceOf[WekaAttributeType])
    assert(WekaNominalAttribute("a", List("a", "b")).isInstanceOf[WekaNominalAttributeType])
    assert(WekaBooleanAttribute("a").isInstanceOf[WekaAttributeType])
    assert(WekaBooleanAttribute("a").isInstanceOf[WekaNominalAttributeType])
    assert(WekaNumericAttribute("a").isInstanceOf[WekaAttributeType])
    assert(WekaDateAttribute("a", "yyyy-MM-dd'T'HH:mm:ss").isInstanceOf[WekaAttributeType])
  }

}

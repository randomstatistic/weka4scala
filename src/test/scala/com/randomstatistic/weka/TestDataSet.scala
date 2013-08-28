package com.randomstatistic.weka

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import weka.classifiers.trees.J48
import WekaConversions._

class TestDataSet extends FunSuite with BeforeAndAfterEach {

  def getDataFormat: WekaDataFormat = {
    new WekaDataFormat("df", new WekaBooleanAttribute("desired"), List(
      WekaBooleanAttribute("good"),
      WekaBooleanAttribute("meh"),
      WekaNominalAttribute("likes chili", List("always", "sometimes", "never", "allergic")),
      WekaNumericAttribute("stars")
    ))
  }

  val df = getDataFormat
  var ds: WekaDataSet = _

  override def beforeEach() {
    ds = new WekaDataSet(df)
  }

  test("load with header") {
    var called = false
    ds.valueGenerator = (h: Header, line: Array[String]) => {
      called = true
      assert(h.name2Col.keys.toList.sorted === List("desired", "good", "instanceWeight", "meh", "likes chili", "stars").sorted)
      Map()
    }
    ds.loadFromCSVWithHeader("src/test/resources/chili_header.tsv")
    assert(called, "Called the valueGenerator")
  }

  //TODO: test load without header

  test("set weights") {
    ds.loadFromCSVWithHeader("src/test/resources/chili_header.tsv")
    assert(2 === ds.instances.instance(0).weight(), "First instance has weight 2")
    assert(1 === ds.instances.instance(1).weight(), "Second instance has weight 1")
  }


  test("can classify") {
    ds.loadFromCSVWithHeader("src/test/resources/chili_header.tsv")

    // use some classifier, configured for this really tiny training set
    val c = new J48()
    c.setMinNumObj(1)
    c.setUnpruned(true)

    // train
    c.buildClassifier(ds)

    // test
    val testInstance = df.generateInstance(
      Map(
        "good" -> "false",
        "meh" -> "true",
        "likes chili" -> "never",
        "stars" -> "5"))
    testInstance.setDataset(ds)
    //println(c) // if you're curious
    assert("false" === df.classificationName(c.classifyInstance(testInstance)))
    assert(
      Map("true" -> 0.0, "false" -> 1.0)
        === df.classificationDistributionMap(c.distributionForInstance(testInstance)))
  }

}

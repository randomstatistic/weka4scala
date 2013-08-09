package com.randomstatistic.weka

object WekaConversions {
  implicit def WekaAttributeType2Attribute(w: WekaAttributeType) = w.asAttribute

  implicit def WekaDataSet2Instances(w: WekaDataSet) = w.instances
}

name := "weka4scala"

organization := "com.randomstatistic"

version := "0.1"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
     "org.scalatest" %% "scalatest" % "2.0.M5b" % "test",
     "nz.ac.waikato.cms.weka" % "weka-dev" % "3.7.9"
)

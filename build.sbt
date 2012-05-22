version := "1.0.0-SNAPSHOT"

scalaVersion := "2.9.1"

name := "OpenplexusRL"

organization := "net.openplexus"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.2" % "test"

seq(releaseSettings: _*)

publishTo := Some("Git Repository" at "https://github.com/lichtsprung/OpenplexusRL")



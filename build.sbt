
name := "fsync"

version := "0.1"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
		"org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
		)

assemblyJarName in assembly := "fsync.jar"

mainClass in assembly := Some("pl.poznan.put.student.scala.fsync.cli.Fsync")
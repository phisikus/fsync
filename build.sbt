lazy val root = (project in file(".")).
  settings(
    name := "fsync",
    version := "1.0",
    scalaVersion := "2.11.1",
    libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
  )

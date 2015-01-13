package pl.poznan.put.student.scala.fsync.test

import java.io.File

import org.scalatest._
import pl.poznan.put.student.scala.fsync.tree.builder.DirectoryTreeBuilder

class TreeRestoreTest extends FlatSpec with BeforeAndAfter {
  val basePath = new File(".").getAbsolutePath
  val exampleDirectoryName = "testDirectory"
  val directoryTreeBuilder = new DirectoryTreeBuilder()
  val directoryGenerator = new DirectoryGenerator(basePath)

  before {
    directoryGenerator.generateExampleDirectory(exampleDirectoryName)
  }

  after {
    directoryGenerator.removeExampleDirectory(exampleDirectoryName)
  }

  "Directory Tree" should "be created properly" in {
    val directoryTree = directoryTreeBuilder.generateTree(basePath + "/" + exampleDirectoryName)
  }


}

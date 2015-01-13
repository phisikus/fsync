package pl.poznan.put.student.scala.fsync.test

import java.io.File

import org.scalatest._
import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.tree.builder.DirectoryTreeBuilder
import pl.poznan.put.student.scala.fsync.tree.difference.generator.BasicDifferenceGenerator

class TreeRestoreTest extends FlatSpec with BeforeAndAfter {
  val basePath = new File(".").getAbsolutePath
  val exampleDirectoryName = "testDirectory"
  val exampleDirectoryPath = basePath + "/" + exampleDirectoryName
  val directoryTreeBuilder = new DirectoryTreeBuilder()
  val directoryGenerator = new DirectoryGenerator(basePath)
  val differenceGenerator = new BasicDifferenceGenerator()
  var generatedDirectoryTree: DirectoryTree = null

  before {
    generatedDirectoryTree = directoryGenerator.generateExampleDirectory(exampleDirectoryName)
  }

  after {
    directoryGenerator.removeExampleDirectory(exampleDirectoryName)
    directoryGenerator.removeExampleDirectory(exampleDirectoryName + "_")
  }

  "Directory Tree" should "be created properly" in {
    val directoryTree = directoryTreeBuilder.generateTree(exampleDirectoryPath)
    assert(directoryTree.equals(generatedDirectoryTree))
  }

  "Directory Tree" should "be restored properly" in {
    replaceTestDirectoryWithEmpty()
    val emptyDirectoryTree = directoryTreeBuilder.generateTree(exampleDirectoryPath)
    restoreTestDirectory()
    val difference = differenceGenerator.generate(emptyDirectoryTree, generatedDirectoryTree)
    replaceTestDirectoryWithEmpty()
    difference.apply()
    val restoredTree = directoryTreeBuilder.generateTree(exampleDirectoryPath)
    assert(generatedDirectoryTree.equals(restoredTree))
  }


  def restoreTestDirectory() {
    directoryGenerator.renameFile(exampleDirectoryName + "_", exampleDirectoryName)
  }

  def replaceTestDirectoryWithEmpty() {
    directoryGenerator.renameFile(exampleDirectoryName, exampleDirectoryName + "_")
    directoryGenerator.removeExampleDirectory(exampleDirectoryName)
    directoryGenerator.createEmptyDirectory(exampleDirectoryName)
  }
}

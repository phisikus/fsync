package pl.poznan.put.student.scala.fsync.test

import java.io.File

import org.scalatest._
import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.tree.builder.DirectoryTreeBuilder
import pl.poznan.put.student.scala.fsync.tree.difference.generator.BasicDifferenceGenerator
import pl.poznan.put.student.scala.fsync.tree.nodes.DirectoryNode

class TreeRestoreTest extends FlatSpec with BeforeAndAfter {
  val basePath = new File(".").getAbsolutePath
  val exampleDirectoryName = "testDirectory"
  val exampleDirectoryPath = basePath + "/" + exampleDirectoryName
  val directoryTreeBuilder = new DirectoryTreeBuilder()
  val directoryGenerator = new DirectoryGenerator(basePath, 1000, 3)
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

  "Directory Tree" should "be changed properly" in {
    directoryGenerator.changeFiles(generatedDirectoryTree.root.asInstanceOf[DirectoryNode])
    val directoryTree = directoryTreeBuilder.generateTree(exampleDirectoryPath)
    val difference = differenceGenerator.generate(generatedDirectoryTree, directoryTree)
    difference.apply()
    val treeAfter = directoryTreeBuilder.generateTree(exampleDirectoryPath)
    val differenceAfter = differenceGenerator.generate(directoryTree, treeAfter)
    assert(differenceAfter.nodeDifferences.isEmpty)
  }

  "Directory Tree" should "be removed properly" in {
    replaceTestDirectoryWithEmpty()
    val emptyDirectoryTree = directoryTreeBuilder.generateTree(exampleDirectoryPath)
    restoreTestDirectory()
    val difference = differenceGenerator.generate(generatedDirectoryTree, emptyDirectoryTree)
    difference.apply()
    val removedTree = directoryTreeBuilder.generateTree(exampleDirectoryPath)
    assert(emptyDirectoryTree.equals(removedTree))

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

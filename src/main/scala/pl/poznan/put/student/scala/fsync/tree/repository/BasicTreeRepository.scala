package pl.poznan.put.student.scala.fsync.tree.repository

import java.io.File

import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.utils.Container

class BasicTreeRepository extends TreeRepository {
  var forest: List[DirectoryTree] = List()

  override def getDirectoryTree(directoryName: String): DirectoryTree = {
    val trees = forest.filter(x => x.path.equals(directoryName))
    trees.length match {
      case x if x > 0 => trees(0)
      case _ => generateEmptyTree(directoryName)
    }
  }

  private def generateEmptyTree(name: String): DirectoryTree = {
    val file = new File(name)
    if (!file.exists) {
      file.mkdirs()
    }
    Container.getTreeBuilder.generateTree(name)
  }

  override def rebuildDirectoryTree(directoryName: String): Unit = {
    forest = forest.filter(x => !x.path.equals(directoryName))
    forest = forest :+ Container.getTreeBuilder.generateTree(directoryName)
  }
}

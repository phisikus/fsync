package pl.poznan.put.student.scala.fsync.tree.builder

import java.nio.file.Path

import pl.poznan.put.student.scala.fsync.tree.{TreeNode, DirectoryTree}
import pl.poznan.put.student.scala.fsync.tree.nodes.DirectoryNode

class DirectoryTreeBuilder extends TreeBuilder {

  def generateNodes(startingPath : Path, rootNode : TreeNode) = {

  }
  override def generateTree(fullPath: Path): DirectoryTree = {
    val root = new DirectoryNode(null, fullPath.getFileName.toString, List())
    val tree = new DirectoryTree(fullPath, root)
    generateNodes(fullPath, root)
    return tree
  }
}

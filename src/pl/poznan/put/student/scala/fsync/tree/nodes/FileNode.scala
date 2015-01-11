package pl.poznan.put.student.scala.fsync.tree.nodes

import pl.poznan.put.student.scala.fsync.tree.TreeNode


class FileNode(parentNode: TreeNode, fileName: String, fileChecksum: String) extends TreeNode {

  override def parent: TreeNode = parentNode

  override var children: List[TreeNode] = List()

  override def hash: String = fileChecksum

  override def name: String = fileName

  override def toString: String = {
    "\n{ name: " + name + ", isDirectory: " + isDirectory + ", hash: " + hash + " }"
  }

  override def isDirectory: Boolean = false
}

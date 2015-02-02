package pl.poznan.put.student.scala.fsync.tree.nodes

import pl.poznan.put.student.scala.fsync.tree.TreeNode


class FileNode(parentNode: TreeNode, fileName: String, fileChecksum: String) extends TreeNode {
  override var children: List[TreeNode] = List()
  override var parent: TreeNode = parentNode

  override def toString: String = {
    "\n{ name: " + name + ", isDirectory: " + isDirectory + ", hash: " + hash + " }"
  }

  override def hash: String = fileChecksum

  override def name: String = fileName

  override def isDirectory: Boolean = false
}

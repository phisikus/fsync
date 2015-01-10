package pl.poznan.put.student.scala.fsync.tree.nodes

import pl.poznan.put.student.scala.fsync.tree.TreeNode


class FileNode(parentNode: TreeNode, fileName: String, fileChecksum: String) extends TreeNode {

  override def parent: TreeNode = parentNode

  override def children: List[TreeNode] = List()

  override def hash: String = fileChecksum

  override def name: String = fileName

  override def toString: String = {
    "{ name: " + name + ", hash: " + hash + " }"
  }
}

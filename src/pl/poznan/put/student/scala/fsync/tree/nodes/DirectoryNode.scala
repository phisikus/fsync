package pl.poznan.put.student.scala.fsync.tree.nodes

import pl.poznan.put.student.scala.fsync.tree.TreeNode
import pl.poznan.put.student.scala.fsync.utils._

class DirectoryNode(parentNode: TreeNode, directoryName: String, childNodes: List[TreeNode]) extends TreeNode {
  var _children: List[TreeNode] = childNodes

  override def children = _children

  override def parent: TreeNode = parentNode

  override def hash: String = {
    val listOfHashes = children.map((node) => node.hash)
    var combinedHashes = ""
    if (listOfHashes.nonEmpty) {
      listOfHashes.reduce((result, element) => {
        result + element
      })
    }
    combinedHashes = combinedHashes + name
    CurrentHashGenerator.get.getHashFromString(combinedHashes)
  }

  override def name: String = directoryName

}

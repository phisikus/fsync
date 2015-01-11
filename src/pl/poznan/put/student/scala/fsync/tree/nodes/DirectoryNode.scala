package pl.poznan.put.student.scala.fsync.tree.nodes

import pl.poznan.put.student.scala.fsync.tree.TreeNode
import pl.poznan.put.student.scala.fsync.utils._

class DirectoryNode(parentNode: TreeNode, directoryName: String, childNodes: List[TreeNode]) extends TreeNode {
  override var children: List[TreeNode] = childNodes

  override def parent: TreeNode = parentNode

  override def hash: String = {
    val listOfHashes = children.map((node) => node.hash)
    var combinedHashes = ""
    if (listOfHashes.nonEmpty) {
      combinedHashes = listOfHashes.reduce((result, element) => {
        result + element
      })
    }
    combinedHashes = combinedHashes + name
    Container.getHashGenerator.generate(combinedHashes)
  }

  override def name: String = directoryName

}

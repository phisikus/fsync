package pl.poznan.put.student.scala.fsync.tree.nodes

import pl.poznan.put.student.scala.fsync.tree.TreeNode
import pl.poznan.put.student.scala.fsync.utils._

class DirectoryNode(parentNode: TreeNode, directoryName: String, childNodes: List[TreeNode]) extends TreeNode {
  override var children: List[TreeNode] = childNodes

  var _hash: String = _

  override def parent: TreeNode = parentNode

  override def hash: String = {
    if(_hash == null)
      this.generateHash
    _hash
  }

  def generateHash: String = {
    val listOfHashes = children.map((node) => node.hash)
    var combinedHashes = ""
    if (listOfHashes.nonEmpty) {
      combinedHashes = listOfHashes.reduce((result, element) => {
        result + element
      })
    }
    combinedHashes = combinedHashes + name
    _hash = Container.getHashGenerator.generate(combinedHashes)
    _hash
  }

  override def name: String = directoryName

  override def isDirectory: Boolean = true
}

package pl.poznan.put.student.scala.fsync.tree.nodes

import pl.poznan.put.student.scala.fsync.tree.TreeNode
import pl.poznan.put.student.scala.fsync.utils._

class DirectoryNode(parentNode: TreeNode, directoryName: String, childNodes: List[TreeNode]) extends TreeNode {
  override var children: List[TreeNode] = childNodes
  private var _hash: String = _

  override var parent: TreeNode = parentNode

  override def hash: String = {
    if (_hash == null)
      this.generateHash
    _hash
  }

  private def generateHash: String = {
    val hashGenerator = Container.getHashGenerator
    val listOfHashes = children.map((node) => hashGenerator.generate(node.name + node.hash))

    def combineHashes(list: List[String]): String = {
      if (listOfHashes.nonEmpty) {
        listOfHashes.reduce((result, element) => {
          result + element
        }) + name
      } else
        name
    }
    _hash = hashGenerator.generate(combineHashes(listOfHashes))
    _hash
  }

  override def name: String = directoryName

  override def isDirectory: Boolean = true
}

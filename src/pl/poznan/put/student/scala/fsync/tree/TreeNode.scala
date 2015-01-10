package pl.poznan.put.student.scala.fsync.tree

trait TreeNode {
  def parent: TreeNode

  var children: List[TreeNode]

  def name: String

  def hash: String

  override def toString: String = {
    "{ name: " + name + ", hash: " + hash + ", children: " + children.toString() + " }"
  }
}

package pl.poznan.put.student.scala.fsync.tree

trait TreeNode {
  def parent: TreeNode

  var children: List[TreeNode]

  def name: String

  def hash: String

  override def toString: String = {
    "{ name: " + name + ", hash: " + hash + ", children: " + children.toString() + " }\n"
  }

  override def equals(obj: scala.Any): Boolean = {
    val result = false
    val node = obj.asInstanceOf[TreeNode]
    if (node != null) {
      name.equals(node.name) && hash.equals(node.hash) && children.eq(node.children)
    }
    result
  }
}

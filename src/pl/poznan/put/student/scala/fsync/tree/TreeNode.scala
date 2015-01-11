package pl.poznan.put.student.scala.fsync.tree

@SerialVersionUID(1L)
trait TreeNode extends Serializable {
  def parent: TreeNode

  var children: List[TreeNode]

  def name: String

  def hash: String

  override def toString: String = {
    "{ name: " + name + ", hash: " + hash + ", children: " + children.toString() + " }\n"
  }

  override def equals(obj: scala.Any): Boolean = {
    var result = false
    val node = obj.asInstanceOf[TreeNode]
    if (node != null) {
      result = name.equals(node.name) && hash.equals(node.hash) && children.equals(node.children)
      if (node.parent != null) {
        result && node.parent.equals(node.parent)
      } else {
        result
      }
    }
    result
  }
}

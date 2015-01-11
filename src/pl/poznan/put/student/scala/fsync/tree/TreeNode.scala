package pl.poznan.put.student.scala.fsync.tree

@SerialVersionUID(1L)
trait TreeNode extends Serializable {
  def parent: TreeNode

  var children: List[TreeNode]

  def name: String

  def hash: String

  def isDirectory: Boolean

  override def toString: String = {
    "\n{ name: " + name + ", hash: " + hash + ", isDirectory: " + isDirectory + ", children: " + children.toString() + " }\n"
  }

  override def equals(obj: scala.Any): Boolean = {
    var result = false
    val node = obj.asInstanceOf[TreeNode]
    if (node != null) {
      result = name.equals(node.name) && hash.equals(node.hash)
      if (node.parent != null) {
        result && node.parent.equals(node.parent)
      } else {
        result
      }
    }
    result
  }

  def getFullPath : String = {
    if(parent != null) {
      parent.getFullPath + '/' + name
    } else {
      name
    }
  }
}

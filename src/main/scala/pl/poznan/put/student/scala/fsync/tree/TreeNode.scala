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
    def isParentEqualIfPresent(extNode: TreeNode): Boolean = {
      extNode.parent match {
        case _: TreeNode => extNode.parent.getFullPath.equals(parent.getFullPath)
        case _ => parent == null
      }
    }
    val node = obj.asInstanceOf[TreeNode]
    node match {
      case n: TreeNode => name.equals(n.name) && hash.equals(n.hash) && isDirectory.equals(n.isDirectory) && isParentEqualIfPresent(n)
      case _ => false
    }

  }

  def getFullPath: String = {
    parent match {
      case p: TreeNode => parent.getFullPath + '/' + name
      case _ => name
    }
  }
}

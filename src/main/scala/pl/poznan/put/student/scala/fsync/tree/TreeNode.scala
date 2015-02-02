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
      case _: TreeNode => name.equals(node.name) && hash.equals(node.hash) && isDirectory.equals(node.isDirectory) && isParentEqualIfPresent(node)
      case _ => false
    }

  }


  override def hashCode(): Int = {
    var hash: Int = 1
    hash = hash * 31 + name.hashCode
    hash = hash * 31 + hash.hashCode
    hash = hash * 31 + isDirectory.hashCode
    hash = hash * 31 + children.length.hashCode
    if (parent != null)
      hash = hash * 31 + parent.hashCode
    hash
  }

  def getFullPath: String = {
    parent match {
      case p: TreeNode => parent.getFullPath + '/' + name
      case _ => name
    }
  }
}

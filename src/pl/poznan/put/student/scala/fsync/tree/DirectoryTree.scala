package pl.poznan.put.student.scala.fsync.tree

import java.nio.file.Path

@SerialVersionUID(1L)
class DirectoryTree(fullPath: String, rootNode: TreeNode) extends Serializable {
  val path: String = fullPath
  val root: TreeNode = rootNode

  def hash: String = {
    root.hash
  }

  override def toString: String = {
    "{\n path: " + path + ",\n hash: " + hash + ",\n root: " + root.toString + "\n}"
  }

  override def equals(obj: scala.Any): Boolean = {
    val tree = obj.asInstanceOf[DirectoryTree]
    if (tree != null) {
      return this.path.equals(tree.path) && this.root.equals(tree.root)
    }
    false
  }
}

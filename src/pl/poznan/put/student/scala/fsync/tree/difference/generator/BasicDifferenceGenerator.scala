package pl.poznan.put.student.scala.fsync.tree.difference.generator

import com.sun.javaws.exceptions.InvalidArgumentException
import pl.poznan.put.student.scala.fsync.tree.difference.types.DeleteFileOrDirectory
import pl.poznan.put.student.scala.fsync.tree.{TreeNode, DirectoryTree}
import pl.poznan.put.student.scala.fsync.tree.difference.{NodeDifference, DifferenceGenerator, TreeDifference}

class BasicDifferenceGenerator extends DifferenceGenerator {

  def removeNodesDifference(nodes: List[TreeNode]): List[NodeDifference] = {
    nodes match {
      case head :: tail =>
        removeNodesDifference(tail) :+ new DeleteFileOrDirectory(head.getFullPath)
      case Nil => List()
    }
  }

  def findByName(name: String, nodes: List[TreeNode]): TreeNode = {
    nodes match {
      case head :: tail =>
        if (head.name.equals(name))
          head
        else
          findByName(name, tail)

      case Nil => null
    }
  }


  def diffNodeList(a: List[TreeNode], b: List[TreeNode]): List[NodeDifference] = {
    val leftDiff = a.diff(b)
    val rightDiff = b.diff(a)

    // types of elements
    // - exist on left side and on right side with different hashes, same names
    //    - if both are files -> update the one on the right
    //    - if left one is file and right is directory -> remove file, add directory recursively
    //    - if left one is directory and right is a file -> remove directory, add right file
    //    - if both are directories -> call this function with their content lists
    // - exists only on left side - remove recursively
    // - exists only on right side - add recursively
    List()

  }

  def generateTreeDifference(sourceRoot: TreeNode, destinationRoot: TreeNode): TreeDifference = {
    val treeDifference = new TreeDifference()
    treeDifference
  }

  override def generate(sourceTree: DirectoryTree, resultTree: DirectoryTree): TreeDifference = {
    if (sourceTree.path != resultTree.path) {
      throw new InvalidArgumentException(Array("Trees relate to different directories"))
    }
    generateTreeDifference(sourceTree.root, resultTree.root)
  }
}

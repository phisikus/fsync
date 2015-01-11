package pl.poznan.put.student.scala.fsync.tree.difference.generator

import com.sun.javaws.exceptions.InvalidArgumentException
import pl.poznan.put.student.scala.fsync.tree.difference.types.{ReplaceContent, CreateFile, CreateDirectory, DeleteFileOrDirectory}
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

  def pairIntersectingNodes(a: List[TreeNode], b: List[TreeNode]): List[Tuple2[TreeNode, TreeNode]] = {
    val allPairs = a.flatMap(x => b.map(y => (x, y)))

    def matchingPairs(pairs: List[Tuple2[TreeNode, TreeNode]]): List[Tuple2[TreeNode, TreeNode]] = {
      pairs match {
        case head :: tail =>
          if (head._1.name.equals(head._2.name))
            List((head._1, head._2)) ::: matchingPairs(tail)
          else
            matchingPairs(tail)
        case Nil => List()
      }
    }
    matchingPairs(allPairs)
  }

  def getContentForNode(node: TreeNode): Array[Byte] = {
    Array() // todo handle retrieving of content
  }


  def handleCommonNodes(list: List[Tuple2[TreeNode, TreeNode]]): List[NodeDifference] = {
    //    - if both are files -> update the one on the right
    //    - if left one is file and right is directory -> remove file, add directory recursively
    //    - if left one is directory and right is a file -> remove directory, add right file
    //    - if both are directories -> call this function with their content lists
    list match {
      case head :: tail =>
        if (!head._1.isDirectory && !head._2.isDirectory)
          return List(new ReplaceContent(head._2.getFullPath, getContentForNode(head._2))) ::: handleCommonNodes(tail)
        if (!head._1.isDirectory && head._2.isDirectory)
          return List(new DeleteFileOrDirectory(head._1.getFullPath)) ::: createNodesDifferences(List(head._2)) ::: handleCommonNodes(tail)
        if (head._1.isDirectory && !head._2.isDirectory)
          return removeNodesDifference(List(head._1)) ::: createNodesDifferences(List(head._2)) ::: handleCommonNodes(tail)
        if (head._1.isDirectory && head._2.isDirectory)
          diffNodeList(head._1.children, head._2.children) ::: handleCommonNodes(tail)
        else
          List()

      case Nil => List()
    }

  }

  def removeNodesDifferences(list: List[TreeNode]): List[NodeDifference] = {
    // nodes exclusive on the left side should be removed, because they don't exist in new version
    def deleteRecursively(nodes: List[TreeNode]): List[NodeDifference] = {
      nodes match {
        case head :: tail => deleteRecursively(tail) :+ new DeleteFileOrDirectory(head.getFullPath)
        case Nil => List()
      }
    }

    list match {
      case head :: tail =>
        deleteRecursively(head :: head.children) ::: removeNodesDifferences(tail)
      case Nil => List()
    }
  }

  def createNodesDifferences(list: List[TreeNode]): List[NodeDifference] = {
    // nodes exclusive on the right side should be added, because they don't exist in old version
    list match {
      case head :: tail =>
        if (head.isDirectory)
          List(new CreateDirectory(head.getFullPath)) ::: createNodesDifferences(head.children ::: tail)
        else
          List(new CreateFile(head.getFullPath, getContentForNode(head))) ::: createNodesDifferences(tail)
      case Nil => List()
    }
  }


  def diffNodeList(a: List[TreeNode], b: List[TreeNode]): List[NodeDifference] = {
    val leftDiff = a.toSet.diff(b.toSet).toList
    val rightDiff = b.toSet.diff(a.toSet).toList
    val intersectingNodesTuples = pairIntersectingNodes(leftDiff, rightDiff)
    val intersectingNodesFlat = intersectingNodesTuples.flatMap(x => List(x._1, x._2))
    val exclusiveLeftNodes = leftDiff.diff(intersectingNodesFlat)
    val exclusiveRightNodes = rightDiff.diff(intersectingNodesFlat)
    handleCommonNodes(intersectingNodesTuples) ::: removeNodesDifferences(exclusiveLeftNodes) ::: createNodesDifferences(exclusiveRightNodes)

  }

  def generateTreeDifference(sourceRoot: TreeNode, destinationRoot: TreeNode): TreeDifference = {
    val treeDifference = new TreeDifference()
    treeDifference.nodeDifferences = diffNodeList(sourceRoot.children, destinationRoot.children)
    treeDifference
  }

  override def generate(sourceTree: DirectoryTree, resultTree: DirectoryTree): TreeDifference = {
    if (sourceTree.path != resultTree.path) {
      throw new InvalidArgumentException(Array("Trees relate to different directories"))
    }
    generateTreeDifference(sourceTree.root, resultTree.root)
  }
}

package pl.poznan.put.student.scala.fsync.tree.difference.generator

import java.nio.file.{Files, Paths}

import pl.poznan.put.student.scala.fsync.tree.difference.types.{CreateDirectory, CreateFile, DeleteFileOrDirectory, ReplaceContent}
import pl.poznan.put.student.scala.fsync.tree.difference.{DifferenceGenerator, NodeDifference, TreeDifference}
import pl.poznan.put.student.scala.fsync.tree.{DirectoryTree, TreeNode}
import pl.poznan.put.student.scala.fsync.utils.BasicThread

import scala.annotation.tailrec

class BasicDifferenceGenerator extends DifferenceGenerator {

  override def generate(sourceTree: DirectoryTree, resultTree: DirectoryTree): TreeDifference = {
    if (sourceTree.path != resultTree.path) {
      throw new IllegalArgumentException("Trees relate to different directories")
    }

    if (sourceTree.equals(resultTree))
      new TreeDifference(sourceTree.path, List())
    else
      generateTreeDifference(sourceTree.root, resultTree.root)
  }

  private def removeNodesDifference(nodes: List[TreeNode]): List[NodeDifference] = {
    nodes match {
      case head :: tail =>
        removeNodesDifference(tail) :+ new DeleteFileOrDirectory(head.getFullPath)
      case Nil => List()
    }
  }

  private def findByName(name: String, nodes: List[TreeNode]): TreeNode = {
    nodes match {
      case head :: tail =>
        if (head.name.equals(name))
          head
        else
          findByName(name, tail)

      case Nil => null
    }
  }

  private def pairIntersectingNodes(a: List[TreeNode], b: List[TreeNode]): List[Tuple2[TreeNode, TreeNode]] = {
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

  private def getContentForNode(node: TreeNode): Array[Byte] = {
    Files.readAllBytes(Paths.get(node.getFullPath))
  }

  private def handleCommonNodes(list: List[Tuple2[TreeNode, TreeNode]]): List[NodeDifference] = {
    //    - if both are files -> update the one on the right
    //    - if left one is file and right is directory -> remove file, add directory recursively
    //    - if left one is directory and right is a file -> remove directory, add right file
    //    - if both are directories -> call this function with their content lists
    list match {
      case head :: tail =>
        if (!head._1.isDirectory && !head._2.isDirectory) {
          if (!head._1.equals(head._2))
            return List(new ReplaceContent(head._2.getFullPath, getContentForNode(head._2))) ::: handleCommonNodes(tail)
          else
            return handleCommonNodes(tail)
        }
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

  private def removeNodesDifferences(list: List[TreeNode]): List[NodeDifference] = {
    // nodes exclusive on the left side should be removed, because they don't exist in new version
    list match {
      case head :: tail =>
        removeNodesDifferences(head.children ::: tail) :+ new DeleteFileOrDirectory(head.getFullPath)
      case Nil => List()
    }
  }

  private def createNodesDifferences(list: List[TreeNode]): List[NodeDifference] = {
    // nodes exclusive on the right side should be added, because they don't exist in old version
    @tailrec
    def createNodeDifferencesTailRec(list: List[TreeNode], acc: List[NodeDifference]): List[NodeDifference] = {
      list match {
        case head :: tail =>
          if (head.isDirectory)
            createNodeDifferencesTailRec(head.children ::: tail, acc ::: List(new CreateDirectory(head.getFullPath)))
          else
            createNodeDifferencesTailRec(tail, acc ::: List(new CreateFile(head.getFullPath, getContentForNode(head))))
        case Nil => acc
      }
    }
    createNodeDifferencesTailRec(list, List())
  }

  private def diffNodeList(a: List[TreeNode], b: List[TreeNode]): List[NodeDifference] = {
    val leftDiff = a.toSet.diff(b.toSet).toList
    val rightDiff = b.toSet.diff(a.toSet).toList
    val intersectingNodesTuples = pairIntersectingNodes(leftDiff, rightDiff)
    val intersectingNodesFlat = intersectingNodesTuples.flatMap(x => List(x._1, x._2))
    val exclusiveLeftNodes = leftDiff.diff(intersectingNodesFlat)
    val exclusiveRightNodes = rightDiff.diff(intersectingNodesFlat)

    val commonNodesDifferenceThread = new BasicThread[List[NodeDifference]](() => handleCommonNodes(intersectingNodesTuples))
    val removeNodesDifferencesList = removeNodesDifferences(exclusiveLeftNodes)
    val createNodesDifferencesList = createNodesDifferences(exclusiveRightNodes)
    commonNodesDifferenceThread.join()

    commonNodesDifferenceThread.result ::: removeNodesDifferencesList ::: createNodesDifferencesList

  }

  private def generateTreeDifference(sourceRoot: TreeNode, destinationRoot: TreeNode): TreeDifference = {
    new TreeDifference(sourceRoot.getFullPath, diffNodeList(sourceRoot.children, destinationRoot.children))
  }
}

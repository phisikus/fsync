package pl.poznan.put.student.scala.fsync.actors

import pl.poznan.put.student.scala.fsync.communication.message._
import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.tree.difference.TreeDifference
import pl.poznan.put.student.scala.fsync.utils.Container

import scala.concurrent.Lock

class Server extends Participant {

  val treeRepository = Container.getTreeRepository
  val differenceGenerator = Container.getDifferenceGenerator
  val pathLock = Container.getPathLock
  var listOfCurrentLocks: List[Lock] = List()

  override def onMessageReceived(msg: Message): Message = {
    msg match {
      case PullMessage(tree) =>
        onPull(tree)
      case PullPushMessage(tree) =>
        onPullPush(tree)
      case PushMessage(diff) =>
        onPush(diff)

      case _ => null
    }
  }

  def onPush(difference: TreeDifference): Message = {
    println(Console.BLUE + "Received difference information about \"" + difference.path + "\" . There are " + difference.nodeDifferences.length.toString + " differences." + Console.RESET)
    println(Console.BLUE + "Applying..." + Console.RESET)
    difference.applyInteractive(false)
    treeRepository.rebuildDirectoryTree(difference.path)
    listOfCurrentLocks = pathLock.release(difference.path, listOfCurrentLocks)
    println(Console.GREEN + "Applied." + Console.RESET)
    PushResponseMessage()
  }

  def onPull(tree: DirectoryTree): Message = {
    listOfCurrentLocks = pathLock.acquire(tree.path, listOfCurrentLocks)
    val difference = generateTreeDifference(tree)
    listOfCurrentLocks = pathLock.release(tree.path, listOfCurrentLocks)
    println(Console.BLUE + "Difference information about \"" + tree.path + "\" created. There are " + difference.nodeDifferences.length.toString + " differences." + Console.RESET)
    PullResponseMessage(difference)
  }

  private def generateTreeDifference(directoryTree: DirectoryTree): TreeDifference = {
    val currentTree = treeRepository.getDirectoryTree(directoryTree.path)
    differenceGenerator.generate(directoryTree, currentTree)
  }

  def onPullPush(tree: DirectoryTree): Message = {
    listOfCurrentLocks = pathLock.acquire(tree.path, listOfCurrentLocks)
    val currentTree = treeRepository.getDirectoryTree(tree.path)
    println(Console.BLUE + "Tree structure of \"" + tree.path + "\" for purpose of client's push created." + Console.RESET)
    PullPushResponseMessage(currentTree)
  }

  override def onInitialize(args: Map[String, String]): Message = {
    null
  }

  override def onCrash(e: Exception): Unit = {
    pathLock.releaseListOfLocks(listOfCurrentLocks)
    println(Console.RED + "Client died." + Console.RESET)
  }
}

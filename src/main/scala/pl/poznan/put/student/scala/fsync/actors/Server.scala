package pl.poznan.put.student.scala.fsync.actors

import pl.poznan.put.student.scala.fsync.communication.message.{Message, MessageType}
import pl.poznan.put.student.scala.fsync.tree.difference.TreeDifference
import pl.poznan.put.student.scala.fsync.utils.Container

import scala.concurrent.Lock

class Server extends Participant {

  val treeRepository = Container.getTreeRepository
  val differenceGenerator = Container.getDifferenceGenerator
  val pathLock = Container.getPathLock
  var listOfCurrentLocks : List[Lock] = List()

  override def onMessageReceived(msg: Message): Message = {
    msg.messageType match {
      case MessageType.Pull =>
        onPull(msg)
      case MessageType.PullPush =>
        onPullPush(msg)
      case MessageType.Push =>
        onPush(msg)

      case _ => null
    }
  }

  def onPush(msg: Message): Message = {
    println(Console.BLUE + "Received difference information about \"" + msg.difference.path + "\" . There are " + msg.difference.nodeDifferences.length.toString + " differences." + Console.RESET)
    println(Console.BLUE + "Applying..." + Console.RESET)
    msg.difference.applyInteractive(false)
    treeRepository.rebuildDirectoryTree(msg.difference.path)
    listOfCurrentLocks = pathLock.release(msg.difference.path, listOfCurrentLocks)
    println(Console.GREEN + "Applied." + Console.RESET)
    new Message(MessageType.PushResponse, null, null)
  }

  def onPull(msg: Message): Message = {
    listOfCurrentLocks = pathLock.acquire(msg.tree.path, listOfCurrentLocks)
    val difference = generateTreeDifferenceFromMessage(msg)
    listOfCurrentLocks = pathLock.release(msg.tree.path, listOfCurrentLocks)
    println(Console.BLUE + "Difference information about \"" + msg.tree.path + "\" created. There are " + difference.nodeDifferences.length.toString + " differences." + Console.RESET)
    new Message(MessageType.PullResponse, null, difference)
  }

  private def generateTreeDifferenceFromMessage(msg: Message): TreeDifference = {
    val currentTree = treeRepository.getDirectoryTree(msg.tree.path)
    differenceGenerator.generate(msg.tree, currentTree)
  }

  def onPullPush(msg: Message): Message = {
    listOfCurrentLocks = pathLock.acquire(msg.tree.path, listOfCurrentLocks)
    val currentTree = treeRepository.getDirectoryTree(msg.tree.path)
    println(Console.BLUE + "Tree structure of \"" + msg.tree.path + "\" for purpose of client's push created." + Console.RESET)
    new Message(MessageType.PullPushResponse, currentTree, null)
  }

  override def onInitialize(args: Map[String, String]): Message = {
    null
  }

  override def onCrush(e: Exception): Unit = {
    pathLock.releaseListOfLocks(listOfCurrentLocks)
    println(Console.RED + "Client died." + Console.RESET)

  }
}

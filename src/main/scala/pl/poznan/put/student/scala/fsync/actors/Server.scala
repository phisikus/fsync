package pl.poznan.put.student.scala.fsync.actors

import pl.poznan.put.student.scala.fsync.communication.message.{Message, MessageType}
import pl.poznan.put.student.scala.fsync.tree.difference.TreeDifference
import pl.poznan.put.student.scala.fsync.utils.Container

class Server extends Participant {

  val treeRepository = Container.getTreeRepository
  val differenceGenerator = Container.getDifferenceGenerator
  val pathLock = Container.getPathLock

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
    pathLock.release(msg.difference.path)
    println(Console.GREEN + "Applied." + Console.RESET)
    new Message(MessageType.PushResponse, null, null)
  }

  def onPull(msg: Message): Message = {
    pathLock.acquire(msg.tree.path)
    val difference = generateTreeDifferenceFromMessage(msg)
    pathLock.release(msg.tree.path)
    println(Console.BLUE + "Difference information about \"" + msg.tree.path + "\" created. There are " + difference.nodeDifferences.length.toString + " differences." + Console.RESET)
    new Message(MessageType.PullResponse, null, difference)
  }

  private def generateTreeDifferenceFromMessage(msg: Message): TreeDifference = {
    val currentTree = treeRepository.getDirectoryTree(msg.tree.path)
    differenceGenerator.generate(msg.tree, currentTree)
  }

  def onPullPush(msg: Message): Message = {
    pathLock.acquire(msg.tree.path)
    val currentTree = treeRepository.getDirectoryTree(msg.tree.path)
    println(Console.BLUE + "Tree structure of \"" + msg.tree.path + "\" for purpose of client's push created." + Console.RESET)
    new Message(MessageType.PullPushResponse, currentTree, null)
  }

  override def onInitialize(args: Map[String, String]): Message = {
    null
  }
}

package pl.poznan.put.student.scala.fsync.actors

import pl.poznan.put.student.scala.fsync.communication.message.{Message, MessageType}
import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.utils.Container

class Client extends Participant {

  val differenceGenerator = Container.getDifferenceGenerator

  override def onMessageReceived(msg: Message): Message = {
    msg.messageType match {
      case MessageType.PullResponse =>
        onPullResponse(msg)
      case MessageType.PullPushResponse =>
        onPullPushResponse(msg)
      case MessageType.PushResponse =>
        onPushResponse(msg)
      case _ =>
        println(Console.RED + "Invalid message." + Console.RESET)
        null
    }
  }

  def onPushResponse(message: Message): Message = {
    println(Console.GREEN + "Pushed successfully." + Console.RESET)
    new Message(MessageType.Goodbye, null, null)
  }

  def onPullPushResponse(msg: Message): Message = {
    println(Console.YELLOW + "Calculating push structures..." + Console.RESET)
    val localTree = getLocalDirectoryTree(msg.tree.path)
    val differenceTree = differenceGenerator.generate(msg.tree, localTree)
    println(Console.GREEN + "Pushing changes...")
    new Message(MessageType.Push, null, differenceTree)
  }

  private def getLocalDirectoryTree(directoryName: String): DirectoryTree = {
    Container.getTreeBuilder.generateTree(directoryName)
  }

  def onPullResponse(msg: Message): Message = {
    println(Console.YELLOW + "Applying changes..." + Console.RESET)
    msg.difference.applyInteractive()
    println(Console.GREEN + "Changes pulled.")
    new Message(MessageType.Goodbye, null, null)
  }

  override def onInitialize(args: Map[String, String]): Message = {
    args("command").toLowerCase match {
      case "pull" =>
        onPull(args)
      case "push" =>
        onPush(args)
      case _ =>
        println(Console.RED + "Invalid action \"" + args("command") + "\" " + Console.RESET)
        null
    }
  }

  def onPush(args: Map[String, String]): Message = {
    val directoryTree = getLocalDirectoryTree(args("directoryName"))
    println(Console.YELLOW + "Pulling metadata for directory before push: " + directoryTree.path + Console.RESET)
    new Message(MessageType.PullPush, directoryTree, null)
  }

  def onPull(args: Map[String, String]): Message = {
    val directoryTree = getLocalDirectoryTree(args("directoryName"))
    println(Console.YELLOW + "Pulling metadata for directory: " + directoryTree.path + Console.RESET)
    new Message(MessageType.Pull, directoryTree, null)
  }

}

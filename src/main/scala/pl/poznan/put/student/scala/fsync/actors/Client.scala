package pl.poznan.put.student.scala.fsync.actors

import pl.poznan.put.student.scala.fsync.communication.message._
import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.tree.difference.TreeDifference
import pl.poznan.put.student.scala.fsync.utils.Container

class Client extends Participant {

  val differenceGenerator = Container.getDifferenceGenerator

  override def onMessageReceived(msg: Message): Message = {
    msg match {
      case PullResponseMessage(diff) =>
        onPullResponse(diff)
      case PullPushResponseMessage(tree) =>
        onPullPushResponse(tree)
      case PushResponseMessage() =>
        onPushResponse()
      case _ =>
        println(Console.RED + "Invalid message." + Console.RESET)
        null
    }
  }

  def onPushResponse(): Message = {
    println(Console.GREEN + "Pushed successfully." + Console.RESET)
    GoodbyeMessage()
  }

  def onPullPushResponse(directoryTree: DirectoryTree): Message = {
    println(Console.YELLOW + "Calculating push structures..." + Console.RESET)
    val localTree = getLocalDirectoryTree(directoryTree.path)
    val differenceTree = differenceGenerator.generate(directoryTree, localTree)
    println(Console.GREEN + "Pushing changes..." + Console.RESET)
    PushMessage(differenceTree)
  }

  private def getLocalDirectoryTree(directoryName: String): DirectoryTree = {
    Container.getTreeBuilder.generateTree(directoryName)
  }

  def onPullResponse(difference: TreeDifference): Message = {
    println(Console.YELLOW + "Applying changes..." + Console.RESET)
    difference.applyInteractive(true)
    println(Console.GREEN + "Changes pulled." + Console.RESET)
    GoodbyeMessage()
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
    PullPushMessage(directoryTree)
  }

  def onPull(args: Map[String, String]): Message = {
    val directoryTree = getLocalDirectoryTree(args("directoryName"))
    println(Console.YELLOW + "Pulling metadata for directory: " + directoryTree.path + Console.RESET)
    PullMessage(directoryTree)
  }

  override def onCrash(e: Exception): Unit = {
    println(Console.RED + "Server connection died." + Console.RESET)
  }
}

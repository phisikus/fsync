package pl.poznan.put.student.scala.fsync.actors

import java.io.File

import pl.poznan.put.student.scala.fsync.communication.message.{MessageType, Message}
import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.utils.Container

class Client extends Participant {

  override def onMessageReceived(msg: Message): Message = {
    msg match {
      case MessageType.PullResponse =>
        println(Console.YELLOW + "Received pull response from server, applying changes..." + Console.RESET)
        println(msg.difference)
        msg.difference.apply()
        println(Console.GREEN + "Changes pulled.")
        null
      case _ =>
        println(Console.RED + "Invalid message." + Console.RESET)
        null
    }
  }

  override def onInitialize(args: Map[String, String]): Message = {
    args("command").toLowerCase match {
      case "pull" =>
        val directoryTree = getLocalDirectoryTree
        println(Console.YELLOW + "Pulling metadata for directory: " + directoryTree.path + Console.RESET)
        new Message(MessageType.Pull, directoryTree, null)
      case "push" =>
        null
      case _ =>
        println(Console.RED + "Invalid action \"" + args("command") + "\" " + Console.RESET)
        null
    }
  }

  private def getLocalDirectoryTree: DirectoryTree = {
    val directoryName = new File(".").getAbsolutePath
    Container.getTreeBuilder.generateTree(directoryName)
  }

}

package pl.poznan.put.student.scala.fsync.actors

import pl.poznan.put.student.scala.fsync.communication.message.{Message, MessageType}
import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.utils.Container

class Client extends Participant {

  override def onMessageReceived(msg: Message): Message = {
    msg.messageType match {
      case MessageType.PullResponse =>
        println(Console.YELLOW + "Applying changes..." + Console.RESET)
        msg.difference.applyInteractive()
        println(Console.GREEN + "Changes pulled.")
        new Message(MessageType.Goodbye, null, null)
      case _ =>
        println(Console.RED + "Invalid message." + Console.RESET)
        null
    }
  }

  override def onInitialize(args: Map[String, String]): Message = {
    args("command").toLowerCase match {
      case "pull" =>
        val directoryTree = getLocalDirectoryTree(args("directoryName"))
        println(Console.YELLOW + "Pulling metadata for directory: " + directoryTree.path + Console.RESET)
        new Message(MessageType.Pull, directoryTree, null)
      case "push" =>
        null
      case _ =>
        println(Console.RED + "Invalid action \"" + args("command") + "\" " + Console.RESET)
        null
    }
  }

  private def getLocalDirectoryTree(directoryName: String): DirectoryTree = {
    Container.getTreeBuilder.generateTree(directoryName)
  }

}

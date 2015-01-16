package pl.poznan.put.student.scala.fsync.actors

import pl.poznan.put.student.scala.fsync.communication.message.{MessageType, Message}
import pl.poznan.put.student.scala.fsync.utils.Container

class Server extends Participant {

  val treeRepository = Container.getTreeRepository
  val differenceGenerator = Container.getDifferenceGenerator

  override def onMessageReceived(msg: Message): Message = {
    msg.messageType match {
      case MessageType.Pull =>
        val currentTree = treeRepository.getDirectoryTree(msg.tree.path)
        val difference = differenceGenerator.generate(msg.tree, currentTree)
        println(Console.BLUE + "Difference information about " + msg.tree.path + " created.")
        println(difference.toString)
        new Message(MessageType.PullResponse, null, difference)
      case _ => null
    }
  }

  override def onInitialize(args: Map[String, String]): Message = {
    null
  }
}

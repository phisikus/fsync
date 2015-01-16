package pl.poznan.put.student.scala.fsync.actors

import pl.poznan.put.student.scala.fsync.communication.message.Message

class Server extends Participant {
  override def onMessageReceived(msg: Message): Message = {
    null
  }

  override def onInitialize(args: Map[String, String]): Message = {
    null
  }
}

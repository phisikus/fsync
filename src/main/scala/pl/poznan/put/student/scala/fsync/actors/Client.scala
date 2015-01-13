package pl.poznan.put.student.scala.fsync.actors

import pl.poznan.put.student.scala.fsync.communication.Communicator
import pl.poznan.put.student.scala.fsync.communication.message.Message

class Client extends Participant {
  override def onMessageReceived(msg: Message, communicator: Communicator): Unit = {

  }

  override def onInitialize(args: Array[String], communicator: Communicator): Unit = {

  }
}

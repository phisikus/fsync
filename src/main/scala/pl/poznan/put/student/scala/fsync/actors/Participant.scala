package pl.poznan.put.student.scala.fsync.actors

import pl.poznan.put.student.scala.fsync.communication.Communicator
import pl.poznan.put.student.scala.fsync.communication.message.Message

trait Participant {

  def onMessageReceived(msg: Message, communicator: Communicator)

  def onInitialize(args: Array[String], communicator: Communicator)

}

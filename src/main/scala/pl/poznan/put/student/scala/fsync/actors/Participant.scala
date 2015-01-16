package pl.poznan.put.student.scala.fsync.actors

import pl.poznan.put.student.scala.fsync.communication.message.Message

trait Participant {

  def onMessageReceived(msg: Message): Message

  def onInitialize(args: Map[String, String]): Message

}

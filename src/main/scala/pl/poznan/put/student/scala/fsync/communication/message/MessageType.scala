package pl.poznan.put.student.scala.fsync.communication.message

object MessageType extends Enumeration {
  type MessageType = Value
  val Pull, PullPush, Push, Response, Goodbye = Value
}

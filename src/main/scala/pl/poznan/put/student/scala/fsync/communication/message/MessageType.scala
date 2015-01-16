package pl.poznan.put.student.scala.fsync.communication.message

object MessageType extends Enumeration {
  type MessageType = Value
  val Pull, Push, PullPush, PullResponse, PushResponse, PullPushResponse, Goodbye = Value
}

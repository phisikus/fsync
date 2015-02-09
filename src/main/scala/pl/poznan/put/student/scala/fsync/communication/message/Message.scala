package pl.poznan.put.student.scala.fsync.communication.message

import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.tree.difference.TreeDifference


abstract class Message extends Serializable {
  var sender: ParticipantHandle = _
  var recipient: ParticipantHandle = _
}

case class GoodbyeMessage() extends Message

case class PushResponseMessage() extends Message

case class PullMessage(directoryTree: DirectoryTree) extends Message

case class PullPushMessage(directoryTree: DirectoryTree) extends Message

case class PullPushResponseMessage(directoryTree: DirectoryTree) extends Message

case class PushMessage(difference: TreeDifference) extends Message

case class PullResponseMessage(difference: TreeDifference) extends Message

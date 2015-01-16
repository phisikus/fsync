package pl.poznan.put.student.scala.fsync.communication.message

import pl.poznan.put.student.scala.fsync.communication.message.MessageType.MessageType
import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.tree.difference.TreeDifference


class Message(msgType: MessageType, directoryTree: DirectoryTree, treeDifference: TreeDifference) extends Serializable {
  var sender: ParticipantHandle = _
  var recipient: ParticipantHandle = _
  val messageType: MessageType = msgType
  val tree: DirectoryTree = directoryTree
  val difference: TreeDifference = treeDifference

}

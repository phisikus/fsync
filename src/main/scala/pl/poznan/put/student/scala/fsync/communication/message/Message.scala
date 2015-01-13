package pl.poznan.put.student.scala.fsync.communication.message

import pl.poznan.put.student.scala.fsync.communication.message.MessageType.MessageType
import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.tree.difference.TreeDifference


class Message extends Serializable {
  var sender: ParticipantHandle = _
  var recipient: ParticipantHandle = _
  var messageType: MessageType = null
  var tree : DirectoryTree = _
  var difference : TreeDifference = _

}

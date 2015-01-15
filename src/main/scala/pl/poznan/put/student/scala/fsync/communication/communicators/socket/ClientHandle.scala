package pl.poznan.put.student.scala.fsync.communication.communicators.socket

import pl.poznan.put.student.scala.fsync.communication.message.ParticipantHandle

class ClientHandle(address: String) extends ParticipantHandle {
  override val hostName: String = address
  override val id: String = null
}

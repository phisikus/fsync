package pl.poznan.put.student.scala.fsync.communication.communicators.socket

import java.net.InetAddress

import pl.poznan.put.student.scala.fsync.communication.message.ParticipantHandle

class ServerHandle extends ParticipantHandle {
  override val hostName: String = "0.0.0.0"
  override val id: String = null
}

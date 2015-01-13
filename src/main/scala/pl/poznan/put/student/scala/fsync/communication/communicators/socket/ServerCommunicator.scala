package pl.poznan.put.student.scala.fsync.communication.communicators.socket

import java.net.ServerSocket

import pl.poznan.put.student.scala.fsync.actors.Participant
import pl.poznan.put.student.scala.fsync.communication.Communicator
import pl.poznan.put.student.scala.fsync.communication.message.ParticipantHandle

class ServerCommunicator(actor: Participant) extends Communicator with Runnable {

  override val participant: Participant = actor
  override val localHandle: ParticipantHandle = new ServerHandle()
  val serverSocket = new ServerSocket(localHandle.port)


  override def run(): Unit = {

  }


}

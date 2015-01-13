package pl.poznan.put.student.scala.fsync.communication.communicators.socket

import java.io.{ObjectOutputStream, ObjectInputStream}
import java.net.ServerSocket

import pl.poznan.put.student.scala.fsync.actors.Participant
import pl.poznan.put.student.scala.fsync.communication.Communicator
import pl.poznan.put.student.scala.fsync.communication.message.{Message, ParticipantHandle}

class ServerCommunicator(actor: Participant) extends Communicator with Runnable {

  override val participant: Participant = actor
  override val localHandle: ParticipantHandle = new ServerHandle()
  val serverSocket = new ServerSocket(localHandle.port)




  override def run(): Unit = {
    while(true) {
      val connectionSocket = serverSocket.accept()
      val inputFromClient = new ObjectInputStream(connectionSocket.getInputStream)
      val messageFromClient = inputFromClient.readObject().asInstanceOf[Message]
      val messageToClient = participant.onMessageReceived(messageFromClient)
      if(messageToClient != null) {
        val outputToClient = new ObjectOutputStream(connectionSocket.getOutputStream)
        outputToClient.writeObject(messageToClient)
      }
      connectionSocket.close()

    }

  }


}

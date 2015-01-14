package pl.poznan.put.student.scala.fsync.communication.communicators.socket

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.Socket

import pl.poznan.put.student.scala.fsync.actors.Participant
import pl.poznan.put.student.scala.fsync.communication.Communicator
import pl.poznan.put.student.scala.fsync.communication.message.{Message, ParticipantHandle}

class ClientCommunicator(actor: Participant, args: Array[String]) extends Communicator {
  override val participant: Participant = actor
  override val localHandle: ParticipantHandle = new ClientHandle()
  var clientSocket: Socket = _


  def initialize() = {
    localHandle.hostName = args(0)
    clientSocket = new Socket(localHandle.hostName, localHandle.port)
    val messageToSend = participant.onInitialize(args)
    if (messageToSend != null) {
      messageToSend.sender = localHandle
      val outputToClient = new ObjectOutputStream(clientSocket.getOutputStream)
      outputToClient.writeObject(messageToSend)
      outputToClient.close()
      var continue = true
      while (continue) {
        val inputFromServer = new ObjectInputStream(clientSocket.getInputStream)
        val messageFromServer = inputFromServer.readObject().asInstanceOf[Message]
        inputFromServer.close()
        val messageToSend = participant.onMessageReceived(messageFromServer)
        if (messageToSend != null) {
          messageToSend.sender = localHandle
          val outputToClient = new ObjectOutputStream(clientSocket.getOutputStream)
          outputToClient.writeObject(messageToSend)
          outputToClient.close()
        } else {
          continue = false
        }
      }
    }
    clientSocket.close()

  }

  initialize()
}

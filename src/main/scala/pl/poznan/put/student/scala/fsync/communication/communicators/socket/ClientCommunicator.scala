package pl.poznan.put.student.scala.fsync.communication.communicators.socket

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.{InetAddress, Socket}

import pl.poznan.put.student.scala.fsync.actors.Participant
import pl.poznan.put.student.scala.fsync.communication.Communicator
import pl.poznan.put.student.scala.fsync.communication.message.{Message, ParticipantHandle}

class ClientCommunicator(actor: Participant, args: Array[String]) extends Communicator {
  override val participant: Participant = actor
  override val localHandle: ParticipantHandle = new ClientHandle()
  val clientHandle : ParticipantHandle = new ClientHandle()
  var clientSocket: Socket = _

  def prepareClientSocket(): Unit = {
    localHandle.hostName = InetAddress.getLocalHost.getHostAddress
    clientHandle.hostName = args(1)
    clientHandle.port = localHandle.port
    clientSocket = new Socket(clientHandle.hostName, clientHandle.port)
  }


  def initialize() = {
    val messageToSend = participant.onInitialize(args)
    if (messageToSend != null) {
      sendMessageToClient(messageToSend)
      var continue = true
      while (continue) {
        val inputFromServer = new ObjectInputStream(clientSocket.getInputStream)
        val messageFromServer = inputFromServer.readObject().asInstanceOf[Message]
        inputFromServer.close()
        val messageToSend = participant.onMessageReceived(messageFromServer)
        if (messageToSend != null) {
          sendMessageToClient(messageToSend)

        } else {
          continue = false
        }
      }
    }
    clientSocket.close()

  }

  def sendMessageToClient(messageToSend: Message) {
    messageToSend.sender = localHandle
    val outputToClient = new ObjectOutputStream(clientSocket.getOutputStream)
    outputToClient.writeObject(messageToSend)
    outputToClient.close()
  }

  initialize()
}

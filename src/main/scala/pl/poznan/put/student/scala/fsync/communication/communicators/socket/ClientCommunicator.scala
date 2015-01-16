package pl.poznan.put.student.scala.fsync.communication.communicators.socket

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.{InetAddress, Socket}

import pl.poznan.put.student.scala.fsync.actors.Participant
import pl.poznan.put.student.scala.fsync.communication.Communicator
import pl.poznan.put.student.scala.fsync.communication.message.{Message, ParticipantHandle}

class ClientCommunicator(actor: Participant, args : Map[String, String]) extends Communicator {
  override val participant: Participant = actor
  override val localHandle: ParticipantHandle = new ClientHandle(InetAddress.getLocalHost.getHostAddress)
  val clientHandle: ParticipantHandle = new ClientHandle(args("address"))

  def prepareClientSocket(): Socket = {
    new Socket(clientHandle.hostName, clientHandle.port)
  }

  def initialize() = {
    val clientSocket = prepareClientSocket()
    val messageToSend = participant.onInitialize(args)
    clientLoop(clientSocket, messageToSend)
    clientSocket.close()
  }

  protected def clientLoop(clientSocket: Socket, messageToServer: Message): Unit = {
    if (messageToServer != null) {
      sendMessageToClient(clientSocket, messageToServer)
      val messageFromServer = getMessageFromServer(clientSocket)
      clientLoop(clientSocket, participant.onMessageReceived(messageFromServer))
    }
  }

  protected def getMessageFromServer(clientSocket: Socket): Message = {
    val inputFromServer = new ObjectInputStream(clientSocket.getInputStream)
    val messageFromServer = inputFromServer.readObject().asInstanceOf[Message]
    inputFromServer.close()
    messageFromServer
  }

  protected def sendMessageToClient(clientSocket: Socket, messageToSend: Message) {
    messageToSend.sender = localHandle
    val outputToClient = new ObjectOutputStream(clientSocket.getOutputStream)
    outputToClient.writeObject(messageToSend)
    outputToClient.close()
  }

}

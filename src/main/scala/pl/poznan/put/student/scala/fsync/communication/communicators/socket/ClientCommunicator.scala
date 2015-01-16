package pl.poznan.put.student.scala.fsync.communication.communicators.socket

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.{InetAddress, Socket}

import pl.poznan.put.student.scala.fsync.actors.Participant
import pl.poznan.put.student.scala.fsync.communication.Communicator
import pl.poznan.put.student.scala.fsync.communication.message.{Message, ParticipantHandle}

class ClientCommunicator(actor: Participant, args: Map[String, String]) extends Communicator {
  override val participant: Participant = actor
  override val localHandle: ParticipantHandle = new ClientHandle(InetAddress.getLocalHost.getHostAddress)
  val clientHandle: ParticipantHandle = new ClientHandle(args("address"))

  def prepareClientSocket(): Socket = {
    new Socket(clientHandle.hostName, clientHandle.port)
  }

  def initialize() = {
    val clientSocket = prepareClientSocket()
    val messageToSend = participant.onInitialize(args)
    val inputFromServer = new ObjectInputStream(clientSocket.getInputStream)
    val outputToServer = new ObjectOutputStream(clientSocket.getOutputStream)
    clientLoop(inputFromServer, outputToServer, messageToSend)
    clientSocket.close()
  }

  protected def clientLoop(inputFromServer: ObjectInputStream, outputToServer: ObjectOutputStream, messageToServer: Message): Unit = {
    if (messageToServer != null) {
      sendMessageToServer(outputToServer, messageToServer)
      val messageFromServer = getMessageFromServer(inputFromServer)
      clientLoop(inputFromServer, outputToServer, participant.onMessageReceived(messageFromServer))
    }
  }

  protected def getMessageFromServer(inputFromServer: ObjectInputStream): Message = {
    val messageFromServer = inputFromServer.readObject().asInstanceOf[Message]
    inputFromServer.close()
    messageFromServer
  }

  protected def sendMessageToServer(outputToServer: ObjectOutputStream, messageToSend: Message) {
    messageToSend.sender = localHandle
    outputToServer.writeObject(messageToSend)
    outputToServer.flush()
    outputToServer.close()
  }

}

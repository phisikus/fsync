package pl.poznan.put.student.scala.fsync.communication.communicators.socket

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.{InetAddress, Socket}

import pl.poznan.put.student.scala.fsync.actors.Participant
import pl.poznan.put.student.scala.fsync.communication.Communicator
import pl.poznan.put.student.scala.fsync.communication.message.{GoodbyeMessage, Message, ParticipantHandle}

class ClientCommunicator(actor: Participant, args: Map[String, String]) extends Communicator {
  override val participant: Participant = actor
  override val localHandle: ParticipantHandle = new ClientHandle(InetAddress.getLocalHost.getHostAddress)
  val clientHandle: ParticipantHandle = new ClientHandle(args("address"))

  def prepareClientSocket(): Socket = {
    new Socket(clientHandle.hostName, clientHandle.port)
  }

  def initialize() = {
    try {
      val clientSocket = prepareClientSocket()
      val messageToSend = participant.onInitialize(args)
      val outputToServer = new ObjectOutputStream(clientSocket.getOutputStream)
      val inputFromServer = new ObjectInputStream(clientSocket.getInputStream)
      clientLoop(inputFromServer, outputToServer, messageToSend)
      inputFromServer.close()
      outputToServer.flush()
      outputToServer.close()
      clientSocket.close()
    }
    catch {
      case e: Exception =>
        participant.onCrash(e)
    }
  }

  protected def clientLoop(inputFromServer: ObjectInputStream, outputToServer: ObjectOutputStream, messageToServer: Message): Unit = {
    if (messageToServer != null) {
      sendMessageToServer(outputToServer, messageToServer)
      messageToServer match {
        case GoodbyeMessage() =>
          println(Console.BLUE + "Sent message " + messageToServer.getClass.getSimpleName + " to server.")
          val messageFromServer = getMessageFromServer(inputFromServer)
          println(Console.BLUE + "Received " + messageFromServer.getClass.getSimpleName + " message from server.")
          clientLoop(inputFromServer, outputToServer, participant.onMessageReceived(messageFromServer))
        case _ =>

      }
    }
  }

  protected def getMessageFromServer(inputFromServer: ObjectInputStream): Message = {
    val messageFromServer = inputFromServer.readObject().asInstanceOf[Message]
    messageFromServer
  }

  protected def sendMessageToServer(outputToServer: ObjectOutputStream, messageToSend: Message) {
    messageToSend.sender = localHandle
    outputToServer.writeObject(messageToSend)
    outputToServer.flush()
  }

}

package pl.poznan.put.student.scala.fsync.communication.communicators.socket

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.{ServerSocket, Socket}

import pl.poznan.put.student.scala.fsync.actors.Participant
import pl.poznan.put.student.scala.fsync.communication.Communicator
import pl.poznan.put.student.scala.fsync.communication.message.{Message, ParticipantHandle}

class ServerCommunicator(actor: Participant, args: Map[String, String]) extends Communicator with Runnable {

  override val participant: Participant = actor
  override val localHandle: ParticipantHandle = new ServerHandle()

  override def run(): Unit = {
    this.initialize()
  }

  def serverLoop(serverSocket: ServerSocket) {
    val connectionSocket = serverSocket.accept()
    println(Console.GREEN + "Accepted connection from " + connectionSocket.getRemoteSocketAddress.toString + Console.RESET)
    handleConnectionInNewThread(connectionSocket)
    serverLoop(serverSocket)
  }

  def initialize(): Unit = {
    println(Console.GREEN + "Initializing server " + localHandle.hostName + ":" + localHandle.port.toString + " ..." + Console.RESET)
    participant.onInitialize(args)
    println(Console.GREEN + "Server ready." + Console.RESET)
    val serverSocket = new ServerSocket(localHandle.port)
    serverLoop(serverSocket)
  }

  def handleConnectionInNewThread(connectionSocket: Socket) {
    new Thread(new Runnable {
      override def run() = {
        try {
          val inputFromClient = new ObjectInputStream(connectionSocket.getInputStream)
          val outputToClient = new ObjectOutputStream(connectionSocket.getOutputStream)
          onClientConnected(inputFromClient, outputToClient)
          inputFromClient.close()
          outputToClient.flush()
          outputToClient.close()
          connectionSocket.close()
          println(Console.RED + "Connection closed with " + connectionSocket.getRemoteSocketAddress.toString + Console.RESET)
        } catch {
          case e: Exception =>
            participant.onCrash(e)
            println(Console.RED_B + "Connection closed with " + connectionSocket.getRemoteSocketAddress.toString + Console.RESET)
        }
      }
    }).run()
  }

  def onClientConnected(inputStream: ObjectInputStream, outputStream: ObjectOutputStream) {
    val messageToClient = receiveMessageAndGetResponseFromActor(inputStream)
    if (messageToClient != null) {
      sendMessageToClient(outputStream, messageToClient)
      onClientConnected(inputStream, outputStream)
    }

  }

  def receiveMessageAndGetResponseFromActor(inputStream: ObjectInputStream): Message = {
    val messageFromClient = getMessageFromClient(inputStream)
    println(Console.BLUE + "Received " + messageFromClient.messageType.toString + " message from client." + Console.RESET)
    val messageToClient = participant.onMessageReceived(messageFromClient)
    if (messageToClient != null) {
      messageToClient.sender = localHandle
      return messageToClient
    }
    null
  }

  def sendMessageToClient(outputToClient: ObjectOutputStream, messageToClient: Message) {
    outputToClient.writeObject(messageToClient)
    outputToClient.flush()
    println(Console.BLUE + "Sent " + messageToClient.messageType.toString + " message to client." + Console.RESET)
  }

  def getMessageFromClient(inputFromClient: ObjectInputStream): Message = {
    val messageFromClient = inputFromClient.readObject().asInstanceOf[Message]
    messageFromClient
  }
}

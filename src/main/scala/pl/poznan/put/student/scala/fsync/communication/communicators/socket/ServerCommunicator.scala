package pl.poznan.put.student.scala.fsync.communication.communicators.socket

import java.io.{ObjectOutputStream, ObjectInputStream}
import java.net.{Socket, ServerSocket}

import pl.poznan.put.student.scala.fsync.actors.Participant
import pl.poznan.put.student.scala.fsync.communication.Communicator
import pl.poznan.put.student.scala.fsync.communication.message.{Message, ParticipantHandle}

class ServerCommunicator(actor: Participant, args: Array[String]) extends Communicator with Runnable {

  override val participant: Participant = actor
  override val localHandle: ParticipantHandle = new ServerHandle()
  val serverSocket = new ServerSocket(localHandle.port)


  override def run(): Unit = {
    println(Console.GREEN + "Initializing server " + localHandle.hostName + ":" + localHandle.port.toString + " ..." + Console.RESET)
    participant.onInitialize(args)
    println(Console.GREEN + "Server ready." + Console.RESET)
    while (true) {
      val connectionSocket = serverSocket.accept()
      handleConnectionInNewThread(connectionSocket)
    }

  }


  def initialize(): Unit = {
    this.run()
  }

  def handleConnectionInNewThread(connectionSocket: Socket) {
    new Thread(new Runnable {
      override def run() = {
        onClientConnected(connectionSocket)
      }
    }).run()
  }

  def onClientConnected(connectionSocket: Socket) {
    println(Console.YELLOW + "Accepted connection from " + connectionSocket.getRemoteSocketAddress.toString + Console.RESET)
    val messageFromClient = getMessageFromClient(connectionSocket)
    messageFromClient.sender = new ClientHandle()
    messageFromClient.sender.hostName = connectionSocket.getRemoteSocketAddress.toString
    println(Console.BLUE + "Received " + messageFromClient.messageType.toString + " message from " + connectionSocket.getRemoteSocketAddress.toString + Console.RESET)
    val messageToClient = participant.onMessageReceived(messageFromClient)
    if (messageToClient != null) {
      val outputToClient = new ObjectOutputStream(connectionSocket.getOutputStream)
      messageToClient.sender = messageFromClient.recipient
      outputToClient.writeObject(messageToClient)
      outputToClient.close()
      println(Console.BLUE + "Sent " + messageToClient.messageType.toString + " to " + connectionSocket.getRemoteSocketAddress.toString + Console.RESET)

    }
    connectionSocket.close()
    println(Console.CYAN + "Connection closed with " + connectionSocket.getRemoteSocketAddress.toString + Console.RESET)
  }

  def getMessageFromClient(connectionSocket: Socket): Message = {
    val inputFromClient = new ObjectInputStream(connectionSocket.getInputStream)
    val messageFromClient = inputFromClient.readObject().asInstanceOf[Message]
    inputFromClient.close()
    messageFromClient
  }
}

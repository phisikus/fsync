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
    val messageToClient = participant.onMessageReceived(messageFromClient)

    if (messageToClient != null) {
      val outputToClient = new ObjectOutputStream(connectionSocket.getOutputStream)
      outputToClient.writeObject(messageToClient)
    }
    connectionSocket.close()
  }

  def getMessageFromClient(connectionSocket: Socket): Message = {
    val inputFromClient = new ObjectInputStream(connectionSocket.getInputStream)
    val messageFromClient = inputFromClient.readObject().asInstanceOf[Message]
    messageFromClient
  }
}

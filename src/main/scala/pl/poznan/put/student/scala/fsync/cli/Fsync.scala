package pl.poznan.put.student.scala.fsync.cli

import pl.poznan.put.student.scala.fsync.actors.Client
import pl.poznan.put.student.scala.fsync.communication.communicators.socket.ClientCommunicator

object Fsync extends App {

  override def main(args: Array[String]): Unit = {
    System.exit(analyzeArguments(args))
  }


  def analyzeArguments(args: Array[String]): Int = {
    if (args.length.equals(0)) {
      println(Console.GREEN + "File synchronization tool [v1.0] El Fartas & Biernacki" + Console.RESET)
      println(Console.YELLOW + "usage: fsync <mode> <command> <address>" + Console.RESET)
      println("<mode>    - server/client")
      println("<command> - pull/push (only client mode)")
      println("<address> - ip address or hostname (only client mode) \n")
      1
    } else {
      args(0) match {
        case "server" => becomeServer(args)
        case "client" => becomeClient(args)
        case _ => 1
      }
    }

  }

  def becomeClient(args: Array[String]): Int = {
    1
    val actor = new Client()
    val communicator = new ClientCommunicator(actor, args(2), args(1))
  }

  def becomeServer(args: Array[String]): Int = {
    1
  }


}

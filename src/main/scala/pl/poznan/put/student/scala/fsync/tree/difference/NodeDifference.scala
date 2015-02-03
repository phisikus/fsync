package pl.poznan.put.student.scala.fsync.tree.difference

import java.util.Scanner


@SerialVersionUID(1L)
trait NodeDifference extends Serializable {


  val path: String

  val operationName: String

  def apply()

  def applyInteractive(ask: Boolean): Boolean = {
    if (ask) {
      println(Console.YELLOW + "Apply operation " + operationName + " on '" + path + "' [(Y)es/(N)o/(A)ll/N(O)ne] ?" + Console.RESET)
      val s: Scanner = new Scanner(System.in)
      s.nextLine.toLowerCase match {
        case "y" =>
          apply()
          true
        case "n" =>
          true
        case "a" =>
          apply()
          false
        case "o" =>
          false
        case _ =>
          applyInteractive(ask)
      }
    } else {
      println("Applying operation " + operationName + " on '" + path + "' ...")
      apply()
      ask
    }
  }


  override def toString: String = {
    "\n{ operationName: " + operationName + ", path: " + path + " }"
  }
}

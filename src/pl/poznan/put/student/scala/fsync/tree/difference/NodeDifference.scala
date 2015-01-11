package pl.poznan.put.student.scala.fsync.tree.difference


@SerialVersionUID(1L)
trait NodeDifference extends Serializable {

  val path: String

  val operationName: String

  def apply()

  override def toString: String = {
    "\n{ operationName: " + operationName + ", path: " + path + " }"
  }
}

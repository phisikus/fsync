package pl.poznan.put.student.scala.fsync.tree.difference


@SerialVersionUID(1L)
trait NodeDifference extends Serializable{

  def getFilePath()

  def apply()
}

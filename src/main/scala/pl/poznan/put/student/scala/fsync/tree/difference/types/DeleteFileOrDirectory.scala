package pl.poznan.put.student.scala.fsync.tree.difference.types

import java.io.File

import pl.poznan.put.student.scala.fsync.tree.difference.NodeDifference

class DeleteFileOrDirectory(filePath: String) extends NodeDifference {
  override val path: String = filePath
  override val operationName: String = "delete"

  def deleteFiles(file: List[File]): Unit = {
    file match {
      case head :: tail =>
        head.delete()
        deleteFiles(tail)
      case Nil =>
    }
  }

  override def apply(): Unit = {
    val file = new File(path)
    deleteFiles(List(file))
  }


}

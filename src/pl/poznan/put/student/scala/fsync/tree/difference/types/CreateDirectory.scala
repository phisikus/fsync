package pl.poznan.put.student.scala.fsync.tree.difference.types

import java.io.File

import pl.poznan.put.student.scala.fsync.tree.difference.NodeDifference

class CreateDirectory(filePath: String) extends NodeDifference {
  override val path: String = filePath

  override def apply(): Unit = {
    val parentDirectory = new File(path)
    parentDirectory.mkdirs()
  }

  override val operationName: String = "createDirectory"
}

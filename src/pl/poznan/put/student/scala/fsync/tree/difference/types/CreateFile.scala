package pl.poznan.put.student.scala.fsync.tree.difference.types

import java.io.FileOutputStream

import pl.poznan.put.student.scala.fsync.tree.difference.NodeDifference

class CreateFile(filePath: String, contentArray: Array[Byte]) extends NodeDifference {
  override val path: String = filePath
  val content: Array[Byte] = contentArray

  override def apply(): Unit = {
    val outputStream = new FileOutputStream(path, false)
    outputStream.write(content)
    outputStream.close()
  }

  override val operationName: String = "createFile"
}

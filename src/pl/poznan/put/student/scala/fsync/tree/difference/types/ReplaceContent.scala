package pl.poznan.put.student.scala.fsync.tree.difference.types

import java.io.{FileNotFoundException, File, FileOutputStream}

import pl.poznan.put.student.scala.fsync.tree.difference.NodeDifference

class ReplaceContent(filePath: String, contentArray: Array[Byte]) extends NodeDifference {
  override val path: String = filePath
  val content: Array[Byte] = contentArray

  override def apply(): Unit = {
    val file = new File(path)
    if (!file.exists && !file.isDirectory) {
      throw new FileNotFoundException()
    }
    val outputStream = new FileOutputStream(path, false)
    outputStream.write(content)
    outputStream.close()
  }

  override val operationName: String = "replaceContent"
}

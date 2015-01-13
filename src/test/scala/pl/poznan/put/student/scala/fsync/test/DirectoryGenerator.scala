package pl.poznan.put.student.scala.fsync.test


import java.io.{FileOutputStream, File}

import scala.util.Random

class DirectoryGenerator(currentPath: String) {

  val randomGenerator = new Random()
  val fileSize = 100000

  def generateRandomContent(size: Int): Array[Byte] = {
    val result = new Array[Byte](size)
    randomGenerator.nextBytes(result)
    result
  }

  def generateRandomFiles(currentPath: String, numberOfFiles: Int): Unit = {
    for (i <- 1 until numberOfFiles) {
      val outputStream = new FileOutputStream(currentPath + "/testFile_" + i.toString, false)
      outputStream.write(generateRandomContent(fileSize))
      outputStream.close()
    }

  }

  def generateExampleDirectory(directoryName: String): Unit = {
    val fullDirectoryName = currentPath + "/" + directoryName
    val parentDirectory = new File(fullDirectoryName)
    parentDirectory.mkdirs()
    generateRandomFiles(fullDirectoryName, 10)
  }

  def removeExampleDirectory(directoryName: String): Unit = {
    def delete(f: File) {
      if (f.isDirectory) {
        for (c <- f.listFiles)
          delete(c)
        f.delete()
      } else {
        f.delete()
      }
    }
    delete(new File(currentPath + "/" + directoryName))
  }
}

package pl.poznan.put.student.scala.fsync.test


import java.io.{FileOutputStream, File}

import pl.poznan.put.student.scala.fsync.tree.{DirectoryTree, TreeNode}
import pl.poznan.put.student.scala.fsync.tree.nodes.{DirectoryNode, FileNode}
import pl.poznan.put.student.scala.fsync.utils.Container

import scala.util.Random

class DirectoryGenerator(currentPath: String) {

  val randomGenerator = new Random()
  val fileSize = 100000

  def generateRandomContent(size: Int): Array[Byte] = {
    val result = new Array[Byte](size)
    randomGenerator.nextBytes(result)
    result
  }

  def generateRandomFiles(root: DirectoryNode, currentPath: String, numberOfFiles: Int): Unit = {
    var fileList = List[TreeNode]()
    for (i <- 1 until numberOfFiles) {
      val outputStream = new FileOutputStream(root.getFullPath + "/testFile_" + i.toString, false)
      val content = generateRandomContent(fileSize)
      outputStream.write(content)
      outputStream.close()

      fileList = fileList :+ new FileNode(root, "testFile_" + i.toString, Container.getHashGenerator.generate(content))
    }
    root.children = fileList
  }

  def generateExampleDirectory(directoryName: String): DirectoryTree = {
    val fullDirectoryName = currentPath + "/" + directoryName
    val parentDirectory = new File(fullDirectoryName)
    val rootNode = new DirectoryNode(null, fullDirectoryName, List())
    val directoryTree = new DirectoryTree(fullDirectoryName, rootNode)
    parentDirectory.mkdirs()
    generateRandomFiles(rootNode,fullDirectoryName, 10)
    directoryTree
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

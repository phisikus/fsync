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
    for (i <- 1 to numberOfFiles) {
      val outputStream = new FileOutputStream(root.getFullPath + "/testFile_" + i.toString, false)
      val content = generateRandomContent(fileSize)
      outputStream.write(content)
      outputStream.close()

      fileList = fileList :+ new FileNode(root, "testFile_" + i.toString, Container.getHashGenerator.generate(content))
    }
    root.children = root.children ::: fileList
  }

  def generateRandomDirectories(parent: DirectoryNode, depth: Int): List[DirectoryNode] = {
    var fileList = List[DirectoryNode]()
    for (i <- 1 to depth) {
      createEmptyDirectory(parent.getFullPath + "/testDirectory_" + i.toString)
      fileList = fileList :+ new DirectoryNode(parent, "testDirectory_" + i.toString, List())
    }
    parent.children = parent.children ::: fileList
    fileList
  }

  def generateRandomTree(parent: DirectoryNode, depth: Int): Unit = {
    if (depth > 0) {
      val generatedDirectories = generateRandomDirectories(parent, depth)
      for (directory <- generatedDirectories) {
        generateRandomTree(directory, depth - 1)
        generateRandomFiles(directory, directory.getFullPath, depth)
      }
    }
  }

  def generateExampleDirectory(directoryName: String): DirectoryTree = {
    val fullDirectoryName = currentPath + "/" + directoryName
    val rootNode = new DirectoryNode(null, fullDirectoryName, List())
    val directoryTree = new DirectoryTree(fullDirectoryName, rootNode)
    createEmptyDirectory(fullDirectoryName)
    generateRandomTree(rootNode, 3)
    directoryTree
  }

  def renameFile(fileName: String, newFileName: String): Unit = {
    new File(fileName).renameTo(new File(newFileName))
  }

  def createEmptyDirectory(fullDirectoryName: String) {
    val parentDirectory = new File(fullDirectoryName)
    parentDirectory.mkdirs()
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

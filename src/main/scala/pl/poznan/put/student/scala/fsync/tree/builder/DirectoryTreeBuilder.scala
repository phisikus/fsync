package pl.poznan.put.student.scala.fsync.tree.builder

import java.io.File
import java.nio.file.{Files, Path, Paths}

import pl.poznan.put.student.scala.fsync.tree.nodes.{DirectoryNode, FileNode}
import pl.poznan.put.student.scala.fsync.tree.{DirectoryTree, TreeNode}
import pl.poznan.put.student.scala.fsync.utils.{BasicThread, Container}


class DirectoryTreeBuilder extends TreeBuilder {

  override def generateTree(directoryName: String): DirectoryTree = {
    val rootNode = generateNode(directoryName, null)
    val tree = new DirectoryTree(directoryName, rootNode)
    tree
  }

  def getDirectoryNode(rootNode: TreeNode, path: Path, file: File): DirectoryNode = {
    val listOfChildFiles = file.listFiles.toList.filter((f) => !Files.isSymbolicLink(f.toPath)).sorted
    val detachedChildNodes = buildChildNodes(listOfChildFiles)
    val directoryNode = new DirectoryNode(rootNode, path.getFileName.toString, detachedChildNodes)
    changeParent(detachedChildNodes, directoryNode)
    directoryNode
  }

  def getFileNode(rootNode: TreeNode, path: Path): FileNode = {
    val content = Files.readAllBytes(path)
    val fileNode = new FileNode(rootNode, path.getFileName.toString, Container.getHashGenerator.generate(content))
    fileNode
  }

  private def generateNode(startingPath: String, rootNode: TreeNode): TreeNode = {
    val path = Paths.get(startingPath)
    val file = new File(startingPath)
    if (file.isDirectory) {
      getDirectoryNode(rootNode, path, file)
    } else {
      getFileNode(rootNode, path)
    }
  }

  private def buildChildNodes(list: List[File]): List[TreeNode] = {
    list match {
      case head :: tail =>
        val generateNodeThread = new BasicThread[TreeNode](() => generateNode(head.getPath, null))
        val buildChildNodesThread = new BasicThread[List[TreeNode]](() => buildChildNodes(tail))
        generateNodeThread.join()
        buildChildNodesThread.join()
        List(generateNodeThread.result) ::: buildChildNodesThread.result

      case Nil => List()
    }
  }

  private def changeParent(list: List[TreeNode], parent: TreeNode): Unit = {
    list match {
      case head :: tail =>
        head.parent = parent
        changeParent(tail, parent)
      case Nil => ()
    }
  }


}

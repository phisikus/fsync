package pl.poznan.put.student.scala.fsync.tree.builder

import java.io.File
import java.nio.file.{Paths, Files, Path}

import pl.poznan.put.student.scala.fsync.tree.{TreeNode, DirectoryTree}
import pl.poznan.put.student.scala.fsync.tree.nodes.{FileNode, DirectoryNode}
import pl.poznan.put.student.scala.fsync.utils.Container


class DirectoryTreeBuilder extends TreeBuilder {

  def generateChildren(files: List[File], root: TreeNode): Unit = {
    if (files.length > 0) {
      generateNodes(files.head.getAbsolutePath, root)
      generateChildren(files.tail, root)
    }
  }

  def generateNodesForDirectory(startingPath: Path, rootNode: TreeNode): FileNode = {
    val content = Files.readAllBytes(startingPath)
    val fileNode = new FileNode(rootNode, startingPath.getFileName.toString, Container.getHashGenerator.generate(content))
    rootNode.children = rootNode.children :+ fileNode
    fileNode
  }

  def generateNodesForRegularFile(startingPath: Path, rootNode: TreeNode, file: File): DirectoryNode = {
    val directoryNode = new DirectoryNode(rootNode, startingPath.getFileName.toString, List())
    if (rootNode != null) {
      rootNode.children = rootNode.children :+ directoryNode
    }
    generateChildren(file.listFiles.toList, directoryNode)
    directoryNode
  }


  def generateNodes(startingPath: String, rootNode: TreeNode): TreeNode = {
    val path = Paths.get(startingPath)
    val file = new File(startingPath)
    if (file.isDirectory) {
      generateNodesForRegularFile(path, rootNode, file)
    } else {
      generateNodesForDirectory(path, rootNode)
    }

  }


  override def generateTree(fullPath: String): DirectoryTree = {
    val rootNode = generateNodes(fullPath, null)
    val tree = new DirectoryTree(fullPath, rootNode)
    tree
  }
}

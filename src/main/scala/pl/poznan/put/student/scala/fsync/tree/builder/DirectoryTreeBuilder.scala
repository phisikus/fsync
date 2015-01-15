package pl.poznan.put.student.scala.fsync.tree.builder

import java.io.File
import java.nio.file.{Files, Path, Paths}

import pl.poznan.put.student.scala.fsync.tree.nodes.{DirectoryNode, FileNode}
import pl.poznan.put.student.scala.fsync.tree.{DirectoryTree, TreeNode}
import pl.poznan.put.student.scala.fsync.utils.Container


class DirectoryTreeBuilder extends TreeBuilder {

  private def generateChildren(files: List[File], root: TreeNode): Unit = {
    if (files.length > 0) {
      generateNodes(files.head.getAbsolutePath, root)
      generateChildren(files.tail, root)
    }
  }

  private def generateNodesForDirectory(startingPath: Path, rootNode: TreeNode): FileNode = {
    val content = Files.readAllBytes(startingPath)
    val fileNode = new FileNode(rootNode, startingPath.getFileName.toString, Container.getHashGenerator.generate(content))
    rootNode.children = rootNode.children :+ fileNode
    fileNode
  }

  private def generateEmptyDirectoryNode(startingPath: Path, rootNode: TreeNode): DirectoryNode = {
    if (rootNode != null)
      new DirectoryNode(rootNode, startingPath.getFileName.toString, List())
    else
      new DirectoryNode(null, startingPath.toString, List())
  }

  private def generateNodesForRegularFile(startingPath: Path, rootNode: TreeNode, file: File): DirectoryNode = {
    val directoryNode = generateEmptyDirectoryNode(startingPath, rootNode)

    if (rootNode != null) {
      rootNode.children = rootNode.children :+ directoryNode
    }
    val sortedChildren = file.listFiles.toList.sorted
    generateChildren(sortedChildren, directoryNode)
    directoryNode
  }


  private def generateNodes(startingPath: String, rootNode: TreeNode): TreeNode = {
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

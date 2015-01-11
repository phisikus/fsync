package pl.poznan.put.student.scala.fsync.tree.builder

import java.io.File
import java.nio.file.{Paths, Files, Path}

import pl.poznan.put.student.scala.fsync.tree.{TreeNode, DirectoryTree}
import pl.poznan.put.student.scala.fsync.tree.nodes.{FileNode, DirectoryNode}
import pl.poznan.put.student.scala.fsync.utils.CurrentHashGenerator


class DirectoryTreeBuilder extends TreeBuilder {

  def generateChildren(files: List[File], root: TreeNode): Unit = {
    if (files.length > 0) {
      val path = Paths.get(files.head.getAbsolutePath)
      generateNodes(path, root)
      generateChildren(files.tail, root)
    }
  }


  def generateNodes(startingPath: Path, rootNode: TreeNode): TreeNode = {
    val file = new File(startingPath.toString)
    if (file.isDirectory) {
      val directoryNode = new DirectoryNode(rootNode, startingPath.getFileName.toString, List())
      if (rootNode != null) {
        rootNode.children = rootNode.children :+ directoryNode
      }
      generateChildren(file.listFiles.toList, directoryNode)
      directoryNode
    } else {
      val content = Files.readAllBytes(startingPath)
      val fileNode = new FileNode(rootNode, startingPath.getFileName.toString, CurrentHashGenerator.get.generate(content))
      rootNode.children = rootNode.children :+ fileNode
      fileNode
    }

  }

  override def generateTree(fullPath: Path): DirectoryTree = {
    val rootNode = generateNodes(fullPath, null)
    val tree = new DirectoryTree(fullPath, rootNode)
    tree
  }
}

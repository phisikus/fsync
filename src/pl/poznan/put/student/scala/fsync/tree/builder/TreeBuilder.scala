package pl.poznan.put.student.scala.fsync.tree.builder

import pl.poznan.put.student.scala.fsync.tree.DirectoryTree

trait TreeBuilder {
  def generateTree(fullPath: String): DirectoryTree
}

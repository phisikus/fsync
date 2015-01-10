package pl.poznan.put.student.scala.fsync.tree.builder

import java.nio.file.Path

import pl.poznan.put.student.scala.fsync.tree.DirectoryTree

trait TreeBuilder {
  def generateTree(fullPath: Path): DirectoryTree
}

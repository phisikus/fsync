package pl.poznan.put.student.scala.fsync.tree.difference

import pl.poznan.put.student.scala.fsync.tree.DirectoryTree

trait DifferenceGenerator {
  def generate(sourceTree: DirectoryTree, resultTree: DirectoryTree) : TreeDifference
}

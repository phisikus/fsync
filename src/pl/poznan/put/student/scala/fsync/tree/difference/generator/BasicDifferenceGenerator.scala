package pl.poznan.put.student.scala.fsync.tree.difference.generator

import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.tree.difference.{TreeDifference, DifferenceGenerator}

class BasicDifferenceGenerator extends DifferenceGenerator {
  override def generate(sourceTree: DirectoryTree, resultTree: DirectoryTree): TreeDifference = {
    val treeDifference = new TreeDifference()
    treeDifference
  }
}

package pl.poznan.put.student.scala.fsync.tree.difference.generator

import com.sun.javaws.exceptions.InvalidArgumentException
import pl.poznan.put.student.scala.fsync.tree.DirectoryTree
import pl.poznan.put.student.scala.fsync.tree.difference.{DifferenceGenerator, TreeDifference}

class BasicDifferenceGenerator extends DifferenceGenerator {


  override def generate(sourceTree: DirectoryTree, resultTree: DirectoryTree): TreeDifference = {
    if (sourceTree.path != resultTree.path) {
      throw new InvalidArgumentException(Array("Trees relate to different directories"))
    }
    val treeDifference = new TreeDifference()
    treeDifference
  }
}

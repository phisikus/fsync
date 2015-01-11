package pl.poznan.put.student.scala.fsync.utils

import pl.poznan.put.student.scala.fsync.tree.builder.{TreeBuilder, DirectoryTreeBuilder}

object Container {
  val currentHashGenerator = new MD5HashGenerator()
  val currentTreeBuilder = new DirectoryTreeBuilder()

  def getHashGenerator: HashGenerator = currentHashGenerator
  def getTreeBuilder: TreeBuilder = currentTreeBuilder
}

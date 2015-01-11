package pl.poznan.put.student.scala.fsync.utils

import pl.poznan.put.student.scala.fsync.tree.builder.{TreeBuilder, DirectoryTreeBuilder}
import pl.poznan.put.student.scala.fsync.tree.difference.DifferenceGenerator
import pl.poznan.put.student.scala.fsync.tree.difference.generator.BasicDifferenceGenerator

object Container {
  val currentHashGenerator = new MD5HashGenerator()
  val currentTreeBuilder = new DirectoryTreeBuilder()
  val currentDifferenceGenerator = new BasicDifferenceGenerator()

  def getHashGenerator: HashGenerator = currentHashGenerator

  def getTreeBuilder: TreeBuilder = currentTreeBuilder

  def getDifferenceGenerator: DifferenceGenerator = currentDifferenceGenerator
}

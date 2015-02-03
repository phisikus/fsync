package pl.poznan.put.student.scala.fsync.utils

import pl.poznan.put.student.scala.fsync.tree.builder.{DirectoryTreeBuilder, TreeBuilder}
import pl.poznan.put.student.scala.fsync.tree.difference.DifferenceGenerator
import pl.poznan.put.student.scala.fsync.tree.difference.generator.BasicDifferenceGenerator
import pl.poznan.put.student.scala.fsync.tree.repository.{BasicTreeRepository, TreeRepository}

object Container {
  val currentHashGenerator = new MD5HashGenerator()
  val currentTreeBuilder = new DirectoryTreeBuilder()
  val currentDifferenceGenerator = new BasicDifferenceGenerator()
  val currentRepositoryTree = new BasicTreeRepository()
  val currentPathLock = new PathLock()

  def getHashGenerator: HashGenerator = currentHashGenerator

  def getTreeBuilder: TreeBuilder = currentTreeBuilder

  def getDifferenceGenerator: DifferenceGenerator = currentDifferenceGenerator

  def getTreeRepository: TreeRepository = currentRepositoryTree

  def getPathLock : PathLock = currentPathLock
}

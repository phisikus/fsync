package pl.poznan.put.student.scala.fsync.tree.repository

import pl.poznan.put.student.scala.fsync.tree.DirectoryTree

trait TreeRepository {
  def getDirectoryTree(name : String) : DirectoryTree
}

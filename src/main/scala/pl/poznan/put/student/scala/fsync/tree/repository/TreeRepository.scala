package pl.poznan.put.student.scala.fsync.tree.repository

import pl.poznan.put.student.scala.fsync.tree.DirectoryTree

trait TreeRepository {
  def getDirectoryTree(directoryName: String): DirectoryTree

  def rebuildDirectoryTree(directoryName: String)
}

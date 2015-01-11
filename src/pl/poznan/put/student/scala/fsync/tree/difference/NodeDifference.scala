package pl.poznan.put.student.scala.fsync.tree.difference

import scala.reflect.io.Path


trait NodeDifference {

  def getFilePath()

  def apply()
}

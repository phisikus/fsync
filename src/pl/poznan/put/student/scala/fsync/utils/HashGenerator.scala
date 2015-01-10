package pl.poznan.put.student.scala.fsync.utils


trait HashGenerator {
  def getHashFromString(content: String): String
}

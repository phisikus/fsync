package pl.poznan.put.student.scala.fsync.utils


trait HashGenerator {
  def generate(content: String): String
  def generate(content: Array[Byte]): String
}

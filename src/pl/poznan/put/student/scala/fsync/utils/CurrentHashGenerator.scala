package pl.poznan.put.student.scala.fsync.utils

object CurrentHashGenerator {
  var currentHashGenerator : HashGenerator = new MD5HashGenerator()

  def get: HashGenerator = {
    currentHashGenerator
  }

  def set(hashGenerator: HashGenerator): Unit = {
    currentHashGenerator = hashGenerator
  }

}

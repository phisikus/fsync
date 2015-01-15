package pl.poznan.put.student.scala.fsync.utils

import java.security.MessageDigest


class MD5HashGenerator extends HashGenerator {

  def generate(content: String): String = {
    generate(content.getBytes)
  }

  def generate(content: Array[Byte]): String = {
    val messageDigest = MessageDigest.getInstance("MD5")
    val sum = messageDigest.digest(content).toList

    def toHexString(b: List[Byte]): String = {
      if (b.length > 0) {
        java.lang.Integer.toString((b.head & 0xff) + 0x100, 16).substring(1) + toHexString(b.tail)
      } else {
        ""
      }
    }

    toHexString(sum)
  }

}

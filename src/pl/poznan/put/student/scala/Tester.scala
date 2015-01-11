package pl.poznan.put.student.scala

import java.nio.file.Paths

import pl.poznan.put.student.scala.fsync.tree.builder.DirectoryTreeBuilder

object Tester extends App {
  val treeBuilder = new DirectoryTreeBuilder()
  val tree = treeBuilder.generateTree(Paths.get("/home/phisikus/eagle"))
  println(tree.root.toString)
  println(tree.equals(tree))
}

package pl.poznan.put.student.scala

import java.nio.file.Paths

import pl.poznan.put.student.scala.fsync.tree.builder.{TreeBuilder, DirectoryTreeBuilder}
import pl.poznan.put.student.scala.fsync.utils.Container

object Tester extends App {
  val treeBuilder : TreeBuilder = Container.getTreeBuilder
  val tree = treeBuilder.generateTree(Paths.get("/home/phisikus/Dokumenty"))
  println(tree.root.toString)

}

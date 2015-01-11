package pl.poznan.put.student.scala.fsync.tree.difference


class TreeDifference {
  var nodeDifferences = List[NodeDifference]()

  def apply() = {
    def applyNodeDifferences(list: List[NodeDifference]): Unit = {
      if (list.length > 0) {
        list.head.apply()
        applyNodeDifferences(list.tail)
      }
    }
  }
}

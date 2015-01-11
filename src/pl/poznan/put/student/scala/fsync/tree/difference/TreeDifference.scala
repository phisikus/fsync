package pl.poznan.put.student.scala.fsync.tree.difference


@SerialVersionUID(1L)
class TreeDifference extends Serializable {
  var nodeDifferences = List[NodeDifference]()

  def apply() = {
    def applyNodeDifferences(list: List[NodeDifference]): Unit = {
      if (list.length > 0) {
        list.head.apply()
        applyNodeDifferences(list.tail)
      }
    }
  }

  override def toString: String = {
    "\n{ nodeDifferences: " + nodeDifferences.toString + " }"
  }
}

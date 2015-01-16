package pl.poznan.put.student.scala.fsync.tree.difference


@SerialVersionUID(1L)
class TreeDifference(treePath: String, differences: List[NodeDifference]) extends Serializable {
  val path = treePath
  val nodeDifferences = differences

  def apply() = {
    def applyNodeDifferences(list: List[NodeDifference]): Unit = {
      if (list.length > 0) {
        list.head.apply()
        applyNodeDifferences(list.tail)
      }
    }
    applyNodeDifferences(nodeDifferences)
  }

  def applyInteractive(ask: Boolean): Unit = {
    def applyNodeDifferencesInteractive(list: List[NodeDifference], askQuestion: Boolean): Unit = {
      if (list.length > 0) {
        applyNodeDifferencesInteractive(list.tail, list.head.applyInteractive(askQuestion))
      }
    }

    applyNodeDifferencesInteractive(nodeDifferences, ask)

  }

  override def toString: String = {
    "\n{ nodeDifferences: " + nodeDifferences.toString + " }"
  }
}

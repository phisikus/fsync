package pl.poznan.put.student.scala.fsync.utils


class BasicThread[A](task: () => A) {
  var result : A = _
  val runnable = new Runnable {
    override def run(): Unit = {
      result = task()
    }
  }
  val thread = new Thread(runnable)

  def join() = {
    thread.join()
  }

  thread.start()
}

package pl.poznan.put.student.scala.fsync.utils


import scala.collection.mutable
import scala.concurrent.Lock

class PathLock {
  val syncLock = new Lock
  val locks = new mutable.HashMap[String, Lock]()

  def acquire(path: String, listOfLocks: List[Lock]): List[Lock] = {
    List(acquire(path)) ::: listOfLocks
  }

  def acquire(path: String): Lock = {
    syncLock.acquire()
    if (locks.contains(path)) {
      val lockToAcquire: Lock = locks.get(path).get
      syncLock.release()
      lockToAcquire.acquire()
      lockToAcquire
    } else {
      val lockToAcquire = new Lock()
      locks += (path -> lockToAcquire)
      syncLock.release()
      lockToAcquire.acquire()
      lockToAcquire
    }

  }

  def release(path: String, listOfLocks: List[Lock]): List[Lock] = {
    val lock = release(path)
    listOfLocks.filterNot(p => p.equals(lock))
  }

  def release(path: String): Lock = {
    syncLock.acquire()
    if (locks.contains(path)) {
      val lockToRelease: Lock = locks.get(path).get
      syncLock.release()
      lockToRelease.release()
      lockToRelease
    } else {
      val lockToRelease = new Lock()
      locks += (path -> lockToRelease)
      syncLock.release()
      lockToRelease.release()
      lockToRelease
    }

  }

  def releaseListOfLocks(list: List[Lock]): Unit = {
    list match {
      case head :: tail => head.release()
        releaseListOfLocks(tail)
      case Nil => ()
    }
  }

}

package pl.poznan.put.student.scala.fsync.utils


import scala.collection.mutable
import scala.concurrent.Lock

class PathLock {
  val syncLock = new Lock
  val locks = new mutable.HashMap[String, Lock]()

  def acquire(path: String): Unit = {
    syncLock.acquire()
    if (locks.contains(path)) {
      val lockToAcquire: Lock = locks.get(path).get
      syncLock.release()
      lockToAcquire.acquire()
    } else {
      val lockToAcquire = new Lock()
      locks += (path -> lockToAcquire)
      syncLock.release()
      lockToAcquire.acquire()
    }

  }

  def release(path: String): Unit = {
    syncLock.acquire()
    if (locks.contains(path)) {
      val lockToRelease: Lock = locks.get(path).get
      syncLock.release()
      lockToRelease.release()
    } else {
      val lockToRelease = new Lock()
      locks += (path -> lockToRelease)
      syncLock.release()
      lockToRelease.release()
    }

  }

}

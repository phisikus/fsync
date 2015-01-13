package pl.poznan.put.student.scala.fsync.communication

import pl.poznan.put.student.scala.fsync.actors.Participant
import pl.poznan.put.student.scala.fsync.communication.message.ParticipantHandle


trait Communicator {
  val participant: Participant
  val localHandle: ParticipantHandle

  def initialize()
}

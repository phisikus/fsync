package pl.poznan.put.student.scala.fsync.communication.message

trait ParticipantHandle extends Serializable {
  var port: Int = 4789
  var hostName: String = null
  var id: String = null
}

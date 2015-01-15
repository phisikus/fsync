package pl.poznan.put.student.scala.fsync.communication.message

@SerialVersionUID(1L)
trait ParticipantHandle extends Serializable {
  val port: Int = 4789
  val hostName: String
  val id: String
}

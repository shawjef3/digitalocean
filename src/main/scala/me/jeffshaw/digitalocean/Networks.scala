package me.jeffshaw.digitalocean

case class Networks(
  v4: Seq[NetworkV4],
  v6: Seq[NetworkV6]
)

object Networks {
  val empty =
    Networks(
      v4 = Seq.empty,
      v6 = Seq.empty
    )
}

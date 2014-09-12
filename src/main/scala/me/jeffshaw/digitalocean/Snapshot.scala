package me.jeffshaw.digitalocean

case class Snapshot(
  id: BigInt,
  name: String,
  distribution: String,
  slug: Option[String],
  public: Boolean,
  regions: Seq[Region]
)

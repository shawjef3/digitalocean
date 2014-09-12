package me.jeffshaw.digitalocean

case class Networks(
  v4: Seq[NetworkV4],
  v6: Seq[NetworkV6]
)

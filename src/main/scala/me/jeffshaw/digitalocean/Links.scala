package me.jeffshaw.digitalocean

case class Links(
  pages: Option[Pages],
  actions: Seq[ActionRef]
)

package me.jeffshaw.digitalocean

case class Pages(
  first: Option[String],
  prev: Option[String],
  next: Option[String],
  last: Option[String]
)

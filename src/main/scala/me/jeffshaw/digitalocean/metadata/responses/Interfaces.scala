package me.jeffshaw.digitalocean.metadata.responses

import me.jeffshaw.digitalocean.NetworkType
import me.jeffshaw.digitalocean.metadata.{Ipv4, Ipv6}

case class Interface(
  ipv4: Option[Ipv4],
  anchorIpv4: Option[Ipv4],
  ipv6: Option[Ipv6],
  anchorIpv6: Option[Ipv6],
  mac: String,
  `type`: NetworkType
)

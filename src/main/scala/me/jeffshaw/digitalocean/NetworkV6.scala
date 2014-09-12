package me.jeffshaw.digitalocean

import java.net.Inet6Address

case class NetworkV6(
  ipAddress: Inet6Address,
  gateway: Inet6Address,
  netmask: Option[Inet6Address],
  cidr: Option[Int],
  `type`: NetworkType
)

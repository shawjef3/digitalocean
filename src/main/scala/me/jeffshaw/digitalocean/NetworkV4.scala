package me.jeffshaw.digitalocean

import java.net.Inet4Address

case class NetworkV4(
  ipAddress: Inet4Address,
  gateway: Inet4Address,
  netmask: Option[Inet4Address],
  cidr: Option[Int],
  `type`: NetworkType
)

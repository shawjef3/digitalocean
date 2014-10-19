package me.jeffshaw.digitalocean.metadata

import java.net.Inet6Address

case class Ipv6(
  ip_address: Inet6Address,
  cidr: Short,
  gateway: Inet6Address
)

package me.jeffshaw.digitalocean.metadata

import java.net.{Inet4Address, Inet6Address}

case class FloatingIp(
  ipv4: FloatingIpv4,
  ipv6: FloatingIpv6
)

case class FloatingIpv4(
  active: Boolean,
  ipAddress: Inet4Address
)

case class FloatingIpv6(
  active: Boolean,
  ipAddress: Inet6Address
)

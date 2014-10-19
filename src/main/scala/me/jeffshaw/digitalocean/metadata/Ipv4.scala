package me.jeffshaw.digitalocean.metadata

import java.net.Inet4Address

case class Ipv4(
  ip_address: Inet4Address,
  netmask: String,
  gateway: Inet4Address
)

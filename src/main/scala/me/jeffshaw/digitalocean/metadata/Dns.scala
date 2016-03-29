package me.jeffshaw.digitalocean.metadata

import java.net.InetAddress

case class Dns(nameservers: Seq[InetAddress])

package me.jeffshaw.digitalocean.metadata

import java.net.{InetAddress, Inet6Address}

import org.json4s._
import org.json4s.JsonAST.JString

private[digitalocean] case object InetAddressSerializer extends CustomSerializer[InetAddress](format =>
  (
    {
      case JString(ip) =>
        InetAddress.getByName(ip)
    },
    {
      case address: Inet6Address =>
        JString(address.getHostAddress)
    }
    )
)

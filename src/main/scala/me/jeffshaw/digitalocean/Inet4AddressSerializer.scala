package me.jeffshaw.digitalocean

import java.net.{Inet4Address, InetAddress}

import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

private[digitalocean] case object Inet4AddressSerializer extends CustomSerializer[Inet4Address](format =>
  (
    {
      case JString(ip) =>
        InetAddress.getByName(ip).asInstanceOf[Inet4Address]
    },
    {
      case address: Inet4Address =>
        JString(address.getHostAddress)
    }
  )
)

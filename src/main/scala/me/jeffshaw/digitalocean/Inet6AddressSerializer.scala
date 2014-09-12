package me.jeffshaw.digitalocean

import java.net.{Inet6Address, InetAddress}

import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

case object Inet6AddressSerializer extends CustomSerializer[Inet6Address](format =>
  (
    {
      case JString(ip) =>
        InetAddress.getByName(ip).asInstanceOf[Inet6Address]
    },
    {
      case address: Inet6Address =>
        JString(address.getHostAddress)
    }
  )
)

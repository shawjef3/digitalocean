package me.jeffshaw

import org.json4s._

package object digitalocean {
  private [digitalocean] implicit val formats = {
    DefaultFormats.withBigDecimal +
      InstantSerializer +
      NetworkType.Serializer +
      Status.Serializer +
      Inet4AddressSerializer +
      Inet6AddressSerializer +
      Action.ResourceType.Serializer +
      Action.Status.Serializer +
      Image.Type.Serializer +
      RegionEnum.Serializer +
      Firewall.Source.Address.Serializer +
      responses.Firewall.Protocol.Serializer +
      Firewall.Status.Serializer +
      Firewall.Port.Serializer
  }
}

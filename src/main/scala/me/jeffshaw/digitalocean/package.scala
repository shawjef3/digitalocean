package me.jeffshaw

import org.json4s._

package object digitalocean {
  implicit val formats = {
    DefaultFormats.withBigDecimal +
      InstantSerializer +
      NetworkType.Serializer +
      Status.Serializer +
      Inet4AddressSerializer +
      Inet6AddressSerializer +
      ResourceTypeSerializer
  }

  implicit def Region2RegionEnum(r: Region): RegionEnum = {
    r.asEnum
  }

  implicit def String2RegionEnum(slug: String): RegionEnum = {
    RegionEnum.fromSlug(slug)
  }

  implicit def Size2SizeEnum(r: Size): SizeEnum = {
    r.asEnum
  }

  implicit def String2SizeEnum(slug: String): SizeEnum = {
    SizeEnum.fromSlug(slug)
  }
}

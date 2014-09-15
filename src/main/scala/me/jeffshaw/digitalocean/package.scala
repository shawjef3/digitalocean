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
      ActionResourceTypeSerializer +
      ActionStatusSerializer
  }

  implicit def Region2RegionEnum(r: Region): RegionEnum = {
    r.toEnum
  }

  implicit def String2RegionEnum(slug: String): RegionEnum = {
    RegionEnum.fromSlug(slug)
  }

  implicit def Size2SizeEnum(size: Size): SizeEnum = {
    size.toEnum
  }

  implicit def String2SizeEnum(slug: String): SizeEnum = {
    SizeEnum.fromSlug(slug)
  }

  implicit def DropletCreation2Droplet(dropletCreation: DropletCreation): Droplet = {
    dropletCreation.droplet
  }
}

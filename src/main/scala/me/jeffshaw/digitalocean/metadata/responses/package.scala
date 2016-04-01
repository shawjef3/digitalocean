package me.jeffshaw.digitalocean.metadata

import java.net.InetAddress
import me.jeffshaw.digitalocean.{NetworkType, RegionEnum, metadata}

package object responses {

  private[digitalocean] case class Metadata(
    droplet_id: BigInt,
    hostname: String,
    vendor_data: String,
    public_keys: Seq[String],
    region: RegionEnum,
    interfaces: Interfaces,
    floating_ip: Option[metadata.FloatingIp],
    dns: Dns
  ) {
    def toMetadata: metadata.Metadata = {
      metadata.Metadata(
        droplet_id,
        hostname,
        vendor_data,
        public_keys,
        region,
        interfaces.toInterfaces,
        floating_ip,
        metadata.Dns(dns.nameservers.map(InetAddress.getByName))
      )
    }
  }

  private[digitalocean] case class Dns(nameservers: Seq[String])

  private[digitalocean] case class Interfaces(
    `private`: Seq[Interface],
    public: Seq[Interface]
  ) {
    def toInterfaces: metadata.Interfaces =
      metadata.Interfaces(
        `private`.map(_.toInterface),
        public.map(_.toInterface)
      )
  }

  private[digitalocean] case class Interface(
    ipv4: Option[Ipv4],
    anchor_ipv4: Option[Ipv4],
    ipv6: Option[Ipv6],
    anchor_ipv6: Option[Ipv6],
    mac: String,
    `type`: NetworkType
  ) {
    def toInterface: metadata.Interface =
      metadata.Interface(
        ipv4,
        anchor_ipv4,
        ipv6,
        anchor_ipv6,
        mac,
        `type`
      )
  }

}

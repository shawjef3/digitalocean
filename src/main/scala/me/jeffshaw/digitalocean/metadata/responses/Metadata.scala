package me.jeffshaw.digitalocean.metadata.responses

import java.net.InetAddress
import me.jeffshaw.digitalocean.RegionEnum
import me.jeffshaw.digitalocean.metadata

case class Metadata(
  droplet_id: BigInt,
  hostname: String,
  vendor_data: String,
  public_keys: Seq[String],
  region: RegionEnum,
  interfaces: metadata.Interfaces,
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
      interfaces,
      floating_ip,
      metadata.Dns(dns.nameservers.map(InetAddress.getByName))
    )
  }
}

case class Dns(nameservers: Seq[String])

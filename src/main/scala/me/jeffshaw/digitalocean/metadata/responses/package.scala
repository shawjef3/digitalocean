package me.jeffshaw.digitalocean
package metadata

import java.net._

package object responses {

  private[metadata] case class Metadata(
    droplet_id: BigInt,
    hostname: String,
    vendor_data: String,
    public_keys: Seq[String],
    region: String,
    interfaces: Interfaces,
    dns: Nameservers
  ) {
    def toMetadata: metadata.Metadata = {
      metadata.Metadata(
        droplet_id,
        hostname,
        vendor_data,
        public_keys,
        RegionEnum.fromSlug(region),
        interfaces,
        dns.nameservers.map(InetAddress.getByName)
      )
    }
  }

  private[metadata] case class Nameservers(
    nameservers: Seq[String]
  )
}

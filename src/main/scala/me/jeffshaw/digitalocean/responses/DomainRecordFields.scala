package me.jeffshaw
package digitalocean
package responses

import java.net._

private[digitalocean] case class DomainRecordFields(
  id: BigInt,
  `type`: String,
  name: Option[String],
  data: Option[String],
  priority: Option[Int],
  weight: Option[Int]
) {
  def inetAddress: Option[InetAddress] = {
    for {
      address <- data
    } yield {
      InetAddress.getByName(address)
    }
  }

  def toDomainRecord(domainName: String): dns.DomainRecord = {
    `type` match {
      case dns.A.StringValue =>
        val address = inetAddress.get.asInstanceOf[Inet4Address]
        dns.A(domainName, id, name.get, address)
      case dns.AAAA.StringValue =>
        val address = inetAddress.get.asInstanceOf[Inet6Address]
        dns.AAAA(domainName, id, name.get, address)
      case dns.CNAME.StringValue =>
        dns.CNAME(domainName, id, name.get, data.get)
      case dns.MX.StringValue =>
        dns.MX(domainName, id, data.get, priority.get)
      case dns.TXT.StringValue =>
        dns.TXT(domainName, id, name.get, data.get)
      case dns.SRV.StringValue =>
        dns.SRV(domainName, id, name.get, inetAddress.get, priority.get, weight.get)
      case dns.NS.StringValue =>
        dns.NS(domainName, id, data.get)
    }
  }
}

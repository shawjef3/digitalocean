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
      case "A" =>
        val address = inetAddress.get.asInstanceOf[Inet4Address]
        dns.A(domainName, id, name.get, address)
      case "AAAA" =>
        val address = inetAddress.get.asInstanceOf[Inet6Address]
        dns.AAAA(domainName, id, name.get, address)
      case "CNAME" =>
        dns.CNAME(domainName, id, name.get, data.get)
      case "MX" =>
        dns.MX(domainName, id, data.get, priority.get)
      case "TXT" =>
        dns.TXT(domainName, id, name.get, data.get)
      case "SRV" =>
        dns.SRV(domainName, id, name.get, inetAddress.get, priority.get, weight.get)
      case "NS" =>
        dns.NS(domainName, id, data.get)
    }
  }
}

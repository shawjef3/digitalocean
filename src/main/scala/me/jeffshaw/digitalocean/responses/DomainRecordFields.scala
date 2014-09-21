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

  def toDomainRecord(domainName: String): digitalocean.DomainRecord = {
    `type` match {
      case "A" =>
        val address = inetAddress.get.asInstanceOf[Inet4Address]
        A(domainName, id, name.get, address)
      case "AAAA" =>
        val address = inetAddress.get.asInstanceOf[Inet6Address]
        AAAA(domainName, id, name.get, address)
      case "CNAME" =>
        CNAME(domainName, id, name.get, data.get)
      case "MX" =>
        MX(domainName, id, data.get, priority.get)
      case "TXT" =>
        TXT(domainName, id, name.get, data.get)
      case "SRV" =>
        SRV(domainName, id, name.get, inetAddress.get, priority.get, weight.get)
      case "NS" =>
        NS(domainName, id, data.get)
    }
  }
}

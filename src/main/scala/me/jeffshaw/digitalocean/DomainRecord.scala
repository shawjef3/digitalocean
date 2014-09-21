package me.jeffshaw.digitalocean

import java.net._

import org.json4s._, JsonDSL._

import scala.concurrent._

sealed trait DomainRecord {
  val domainName: String
  val id: BigInt
  val `type`: String

  def delete(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    client.delete("domains", domainName, "records", id.toString)
  }
}

sealed trait DomainRecordWithName extends DomainRecord {
  val name: String

  def setName(
    newName: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    val putBody: JValue = "name" -> newName
    client.put[responses.DomainRecord](putBody, "domains", domainName, "records", id.toString).
      map(_.domainRecord.toDomainRecord(domainName))
  }
}

case class A(
  domainName: String,
  id: BigInt,
  name: String,
  data: Inet4Address
) extends DomainRecordWithName {
  override val `type`: String = "A"
}

case class AAAA(
  domainName: String,
  id: BigInt,
  name: String,
  data: Inet6Address
) extends DomainRecordWithName {
  override val `type`: String = "AAAA"
}

case class CNAME(
  domainName: String,
  id: BigInt,
  name: String,
  data: String
) extends DomainRecordWithName {
  override val `type`: String = "CNAME"
}

case class MX(
  domainName: String,
  id: BigInt,
  data: String,
  priority: Int
) extends DomainRecord {
  override val `type`: String = "MX"
}

case class TXT(
  domainName: String,
  id: BigInt,
  name: String,
  data: String
) extends DomainRecordWithName {
  override val `type`: String = "TXT"
}

case class SRV(
  domainName: String,
  id: BigInt,
  name: String,
  data: InetAddress,
  priority: Int,
  weight: Int
) extends DomainRecordWithName {
  override val `type`: String = "SRV"
}

case class NS(
  domainName: String,
  id: BigInt,
  data: String
) extends DomainRecord {
  override val `type`: String = "NS"
}

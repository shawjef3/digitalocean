package me.jeffshaw.digitalocean
package dns

import java.net._

import org.json4s.JsonDSL._
import org.json4s._

import scala.concurrent._

sealed trait DomainRecord {
  val domainName: String
  val id: BigInt
  val `type`: String

  def delete()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    client.delete(Seq("domains", domainName, "records", id.toString))
  }
}

trait StringValueOfObjectName {
  val StringValue = getClass.getSimpleName.dropRight(1)
}

sealed trait DomainRecordWithName extends DomainRecord {
  val name: String

  def setName(
    newName: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    val putBody: JValue = "name" -> newName
    client.put[responses.DomainRecord](Seq("domains", domainName, "records", id.toString), putBody).
      map(_.domainRecord.toDomainRecord(domainName))
  }
}

case class A(
  domainName: String,
  id: BigInt,
  name: String,
  data: Inet4Address
) extends DomainRecordWithName {
  override val `type`: String = A.StringValue
}

object A extends StringValueOfObjectName

case class AAAA(
  domainName: String,
  id: BigInt,
  name: String,
  data: Inet6Address
) extends DomainRecordWithName {
  override val `type`: String = AAAA.StringValue
}

object AAAA extends StringValueOfObjectName

case class CNAME(
  domainName: String,
  id: BigInt,
  name: String,
  data: String
) extends DomainRecordWithName {
  override val `type`: String = CNAME.StringValue
}

object CNAME extends StringValueOfObjectName

case class MX(
  domainName: String,
  id: BigInt,
  data: String,
  priority: Int
) extends DomainRecord {
  override val `type`: String = MX.StringValue
}

object MX extends StringValueOfObjectName

case class TXT(
  domainName: String,
  id: BigInt,
  name: String,
  data: String
) extends DomainRecordWithName {
  override val `type`: String = TXT.StringValue
}

object TXT extends StringValueOfObjectName

case class SRV(
  domainName: String,
  id: BigInt,
  name: String,
  data: InetAddress,
  priority: Int,
  weight: Int
) extends DomainRecordWithName {
  override val `type`: String = SRV.StringValue
}

object SRV extends StringValueOfObjectName

case class NS(
  domainName: String,
  id: BigInt,
  data: String
) extends DomainRecord {
  override val `type`: String = NS.StringValue
}

object NS extends StringValueOfObjectName

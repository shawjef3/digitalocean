package me.jeffshaw.digitalocean.dns

import java.net._
import me.jeffshaw.digitalocean._
import me.jeffshaw.digitalocean.responses.PagedResponse
import org.json4s.Extraction
import org.json4s.JsonDSL._
import scala.concurrent.{ExecutionContext, Future}

case class Domain(
  name: String,
  ttl: Option[Int],
  zoneFile: Option[String]
) extends Path {

  override protected val path: Seq[String] = Seq("domains", name, "records")

  def delete()(
    implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Domain.delete(name)
  }

  def records()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[DomainRecord]] = {
    val pagedResponse = for {
      response <- client.get[responses.DomainRecords](path)
    } yield {
      PagedResponse[responses.DomainRecordFields, responses.DomainRecords](
        client,
        ec,
        response
      )
    }
    pagedResponse.map(_.iterator.map(_.toDomainRecord(this.name)))
  }

  def deleteRecord(
    recordId: BigInt
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Domain.deleteRecord(this.name, recordId)
  }

  def createA(
    recordName: String,
    recordData: Inet4Address
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    Domain.createA(
    this.name,
    recordName,
    recordData
    )
  }

  def createA(
    recordName: String,
    recordData: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    Domain.createA(
      this.name,
      recordName,
      recordData
    )
  }

  def createAAAA(
    recordName: String,
    recordData: Inet6Address
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    Domain.createAAAA(
      this.name,
      recordName,
      recordData
    )
  }

  def createAAAA(
    recordName: String,
    recordData: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    Domain.createAAAA(
      this.name,
      recordName,
      recordData
    )
  }

  def createCName(
    domainName: String,
    recordName: String,
    recordData: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    Domain.createCName(this.name, recordName, recordData)
  }

  def createMX(
    recordData: String,
    recordPriority: Int
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    Domain.createMX(this.name, recordData, recordPriority)
  }

  def createTxt(
    recordName: String,
    recordData: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    Domain.createTxt(this.name, recordName, recordData)
  }

  def createSrv(
    recordName: String,
    recordData: String,
    recordPriority: Int,
    recordWeight: Int
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    Domain.createSrv(
      this.name,
      recordName,
      recordData,
      recordPriority,
      recordWeight
    )
  }

  def createNS(
    recordData: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    Domain.createNS(this.name, recordData)
  }
}

object Domain extends Path
  with Listable[Domain, responses.Domains] {

  override protected val path: Seq[String] = Seq("domains")

  def apply(
    name: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Domain] = {
    val getPath = path :+ name

    client.get[responses.Domain](getPath).map(_.domain)
  }

  def create(
    name: String,
    ipAddress: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Domain] = {
    create(
      name,
      InetAddress.getByName(ipAddress)
    )
  }

  def create(
    name: String,
    ipAddress: InetAddress
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Domain] = {
    val postBody =
      ("name" -> name) ~
        ("ip_address" -> ipAddress.getHostAddress)

    client.post[responses.Domain](
      path,
      postBody
    ).map(_.domain)
  }

  def delete(
    name: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    val deletePath = path :+ name

    client.delete(deletePath)
  }

  def deleteRecord(
    name: String,
    recordId: BigInt
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    val deletePath = path ++ Seq(name, recordId.toString)
    client.delete(deletePath)
  }

  private case class CreateRecord(
    `type`: String,
    name: Option[String] = None,
    data: String,
    priority: Option[Int] = None,
    port: Option[Int] = None,
    weight: Option[Int] = None
  )

  private def createRecord(
    domainName: String,
    record: CreateRecord
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    val postPath = path ++ Seq(domainName, "records")
    val postBody = Extraction.decompose(record)

    client.post[responses.DomainRecord](postPath, postBody).map(_.domainRecord.toDomainRecord(domainName))
  }

  def createA(
    domainName: String,
    recordName: String,
    recordData: Inet4Address
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    val record = CreateRecord(
      `type` = "A",
      name = Some(recordName),
      data = recordData.getHostAddress
    )

    createRecord(domainName, record)
  }

  def createA(
    domainName: String,
    recordName: String,
    recordData: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    val address = InetAddress.getByName(recordData).asInstanceOf[Inet4Address]

    createA(domainName, recordName, address)
  }

  def createAAAA(
    domainName: String,
    recordName: String,
    recordData: Inet6Address
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    val record = CreateRecord(
      `type` = "AAAA",
      name = Some(recordName),
      data = recordData.getHostAddress
    )

    createRecord(domainName, record)
  }

  def createAAAA(
    domainName: String,
    recordName: String,
    recordData: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    val address = InetAddress.getByName(recordData).asInstanceOf[Inet6Address]

    createAAAA(domainName, recordName, address)
  }

  def createCName(
    domainName: String,
    recordName: String,
    recordData: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    val record = CreateRecord(
      `type` = "CNAME",
      name = Some(recordName),
      data = recordData
    )

    createRecord(domainName, record)
  }

  def createMX(
    domainName: String,
    recordData: String,
    recordPriority: Int
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    if (recordPriority < 0 || recordPriority > 63335) {
      throw new IllegalArgumentException("priority")
    }
    val record = CreateRecord(
      `type` = "MX",
      data = recordData,
      priority = Some(recordPriority)
    )

    createRecord(domainName, record)
  }

  def createTxt(
    domainName: String,
    recordName: String,
    recordData: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    val record = CreateRecord(
      `type` = "TXT",
      name = Some(recordName),
      data = recordData
    )

    createRecord(domainName, record)
  }

  def createSrv(
    domainName: String,
    recordName: String,
    recordData: String,
    recordPriority: Int,
    recordWeight: Int
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    if (recordPriority < 0 || recordPriority > 63335) {
      throw new IllegalArgumentException("recordPriority")
    }

    if (recordWeight < 0 || recordWeight > 63335) {
      throw new IllegalArgumentException("recordWeight")
    }

    val record = CreateRecord(
      `type` = "SRV",
      name = Some(recordName),
      data = recordData,
      priority = Some(recordPriority),
      weight = Some(recordWeight)
    )

    createRecord(domainName, record)
  }

  def createNS(
    domainName: String,
    recordData: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DomainRecord] = {
    val record = CreateRecord(
      `type` = "NS",
      data = recordData
    )

    createRecord(domainName, record)
  }
}

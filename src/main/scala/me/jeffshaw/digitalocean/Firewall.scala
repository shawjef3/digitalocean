package me.jeffshaw.digitalocean

import java.net.InetAddress
import java.time.Instant
import org.json4s.{CustomSerializer, Extraction}
import org.json4s.JsonAST._
import org.json4s.JsonDSL.WithBigDecimal._
import scala.concurrent.{ExecutionContext, Future}

case class Firewall(
  id: String,
  status: Firewall.Status,
  createdAt: Instant,
  pendingChanges: Seq[Firewall.PendingChange],
  name: String,
  inboundRules: Seq[Firewall.InboundRule],
  outboundRules: Seq[Firewall.OutboundRule],
  dropletIds: Seq[BigInt],
  tags: Seq[String]
) {
  def update(
    name: String = name,
    inboundRules: Seq[Firewall.InboundRule] = inboundRules,
    outboundRules: Seq[Firewall.OutboundRule] = outboundRules,
    dropletIds: Seq[BigInt] = dropletIds,
    tags: Seq[String] = tags
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Firewall] = {
    Firewall.update(id, name, inboundRules, outboundRules, dropletIds, tags)
  }

  def addDroplets(
    droplets: Seq[BigInt]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Firewall.addDroplets(id, droplets)
  }

  def removeDroplets(
    droplets: Seq[BigInt]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Firewall.removeDroplets(id, droplets)
  }

  def addTags(
    tags: Seq[String]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Firewall.addTags(id, tags)
  }

  def removeTags(
    tags: Seq[String]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Firewall.removeTags(id, tags)
  }

  def addRules(
    inboundRules: Seq[Firewall.InboundRule],
    outboundRules: Seq[Firewall.OutboundRule]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Firewall.addRules(id, inboundRules, outboundRules)
  }

  def removeRules(
    inboundRules: Seq[Firewall.InboundRule],
    outboundRules: Seq[Firewall.OutboundRule]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Firewall.removeRules(id, inboundRules, outboundRules)
  }

  def delete()(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Firewall.delete(id)
  }
}

object Firewall extends Path with Listable[Firewall, responses.Firewalls] {

  override protected val path: Seq[String] = Seq("firewalls")

  def apply(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Firewall] = {
    val path = this.path :+ id.toString
    client.get[Firewall](path)
  }

  private case class CreateOrUpdate(
    name: String,
    inboundRules: Seq[responses.Firewall.InboundRule],
    outboundRules: Seq[responses.Firewall.OutboundRule],
    dropletIds: Seq[BigInt],
    tags: Seq[String]
  )

  def create(
    name: String,
    inboundRules: Seq[InboundRule],
    outboundRules: Seq[OutboundRule],
    dropletIds: Seq[BigInt] = Seq(),
    tags: Seq[String] = Seq()
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Firewall] = {
    val postJson =
      Extraction.decompose(
        CreateOrUpdate(
          name,
          inboundRules.map(responses.Firewall.InboundRule.valueOf),
          outboundRules.map(responses.Firewall.OutboundRule.valueOf),
          dropletIds,
          tags
        )
      )

    val request = client.post[responses.FirewallCreateOrUpdate](path, postJson)

    for {
      response <- request
    } yield response.firewall.toFirewall
  }

  def update(
    id: String,
    name: String,
    inboundRules: Seq[Firewall.InboundRule],
    outboundRules: Seq[Firewall.OutboundRule],
    dropletIds: Seq[BigInt],
    tags: Seq[String]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Firewall] = {
    val postJson =
      Extraction.decompose(
        CreateOrUpdate(
          name,
          inboundRules.map(responses.Firewall.InboundRule.valueOf),
          outboundRules.map(responses.Firewall.OutboundRule.valueOf),
          dropletIds,
          tags
        )
      )

    val request = client.put[responses.FirewallCreateOrUpdate](path :+ id, postJson)

    for {
      response <- request
    } yield response.firewall.toFirewall
  }

  def addDroplets(
    firewallId: String,
    dropletIds: Seq[BigInt]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    val postBody = "droplet_ids" -> dropletIds

    client.postWithEmptyResponse(path ++ Seq(firewallId, "droplets"), postBody)
  }

  def removeDroplets(
    firewallId: String,
    dropletIds: Seq[BigInt]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    val postBody = "droplet_ids" -> dropletIds

    client.delete(path ++ Seq(firewallId, "droplets"), Some(postBody))
  }

  def addTags(
    firewallId: String,
    tags: Seq[String]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    val postBody = "tags" -> tags

    client.postWithEmptyResponse(path ++ Seq(firewallId, "tags"), postBody)
  }

  def removeTags(
    firewallId: String,
    tags: Seq[String]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    val postBody = "tags" -> tags

    client.delete(path ++ Seq(firewallId, "tags"), Some(postBody))
  }

  def delete(id: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    client.delete(path :+ id)
  }

  private case class AlterRules(
    inboundRules: Option[Seq[Firewall.InboundRule]] = None,
    outboundRules: Option[Seq[Firewall.OutboundRule]] = None
  )

  private object AlterRules {
    def apply(
      inboundRules: Seq[Firewall.InboundRule],
      outboundRules: Seq[Firewall.OutboundRule]
    ): AlterRules = {
      apply(responses.seqToOption(inboundRules), responses.seqToOption(outboundRules))
    }
  }

  def addRules(
    firewallId: String,
    inboundRules: Seq[Firewall.InboundRule] = Seq(),
    outboundRules: Seq[Firewall.OutboundRule] = Seq()
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    val postBody = Extraction.decompose(AlterRules(inboundRules, outboundRules))

    client.postWithEmptyResponse(path ++ Seq(firewallId, "rules"), postBody)
  }

  def removeRules(
    firewallId: String,
    inboundRules: Seq[Firewall.InboundRule] = Seq(),
    outboundRules: Seq[Firewall.OutboundRule] = Seq()
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    val postBody = Extraction.decompose(AlterRules(inboundRules, outboundRules))

    client.delete(path ++ Seq(firewallId, "rules"), Some(postBody))
  }

  case class InboundRule(
    protocol: Protocol,
    sources: Source
  )

  case class OutboundRule(
    protocol: Protocol,
    destinations: Destination
  )

  sealed trait Port

  object Port {
    case class Range(start: Int, end: Int) extends Port

    object Range {
      def valueOf(rangeString: String): Range = {
        val split = rangeString.split('-')
        Range(split(0).toInt, split(1).toInt)
      }
    }

    case class Single(port: Int) extends Port

    case object All extends Port

    private[digitalocean] object Serializer extends CustomSerializer[Port](format =>
      ({
        case JString("all") | JString("0") =>
          All
        case JString(rangeString) if rangeString.contains("-") =>
          Range.valueOf(rangeString)
        case JString(port) =>
          Single(port.toInt)
      },
      {
        case Range(start, end) => JString(s"$start-$end")
        case Single(port) => JString(port.toString)
        case All => JString("all")
      })
    )
  }

  case class Source(
    addresses: Seq[Source.Address] = Seq(),
    dropletIds: Seq[BigInt] = Seq(),
    loadBalancerUids: Seq[String] = Seq(),
    tags: Seq[String] = Seq()
  )

  object Source {
    case class Address(
      address: InetAddress,
      cidr: Option[Int] = None
    )

    object Address {
      private[digitalocean] object Serializer extends CustomSerializer[Address](format =>
        ({
          case JString(str) =>
            val split = str.split('/')
            val address = InetAddress.getByName(split(0))
            val cidr =
              for {
                cidrString <- split.lift(1)
              } yield cidrString.toInt
            Address(address, cidr)
        },
        {
          case Address(address, cidr) =>
            val cidrString = cidr.map("/" + _.toString).getOrElse("")
            JString(address.getHostAddress + cidrString)
        })
      )
    }
  }

  type Destination = Source

  val Destination = Source

  sealed trait Protocol

  object Protocol {
    case class Tcp(port: Port) extends Protocol

    case class Udp(port: Port) extends Protocol

    case object Icmp extends Protocol

  }

  sealed trait Status {
    val StringValue: String
  }

  object Status {
    case object Waiting extends Status {
      override val StringValue: String = "waiting"
    }
    case object Succeeded extends Status {
      override val StringValue: String = "succeeded"
    }
    case object Failed extends Status {
      override val StringValue: String = "failed"
    }

    private[digitalocean] case object Serializer extends CustomSerializer[Status](format =>
      (
        {
          case JString(Waiting.StringValue) => Waiting
          case JString(Succeeded.StringValue) => Succeeded
          case JString(Failed.StringValue) => Failed
        },
        {
          case tpe: Status =>
            JString(tpe.StringValue)
        }
      )
    )
  }

  case class PendingChange(
    dropletId: BigInt,
    removing: Boolean,
    status: Status
  )
}

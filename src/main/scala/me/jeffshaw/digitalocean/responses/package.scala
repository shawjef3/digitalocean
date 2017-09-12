package me.jeffshaw
package digitalocean

import java.time.Instant
import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString
import scala.concurrent.{ExecutionContext, Future}

package object responses {

  private[digitalocean] def seqToOption[T](s: Seq[T]): Option[Seq[T]] = {
    if (s.isEmpty)
      None
    else Some(s)
  }

  private[digitalocean] case class Meta(total: BigInt)

  private[digitalocean] case class Pages(
    first: Option[String],
    prev: Option[String],
    next: Option[String],
    last: Option[String]
  )

  private[digitalocean] case class ActionRef(
    id: BigInt,
    rel: String,
    href: String
  ) {
    def action(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[digitalocean.Action] = {
      digitalocean.Action(id)
    }
  }

  private[digitalocean] case class Links(
    pages: Option[Pages],
    actions: Seq[ActionRef]
  )

  private[digitalocean] sealed trait Page[T] {
    def page: Seq[T]
    val meta: Option[Meta]
    val links: Option[Links]

    def size: Option[BigInt] = {
      meta.map(_.total)
    }
  }

  private [digitalocean] case class Domains(
    domains: Seq[dns.Domain],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[dns.Domain] {
    override def page: Seq[dns.Domain] = domains
  }

  private [digitalocean] case class Domain(
    domain: dns.Domain
  )

  private [digitalocean] case class DomainRecord(
    domainRecord: DomainRecordFields
  )

  private [digitalocean] case class DomainRecords(
    domainRecords: Seq[DomainRecordFields],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[DomainRecordFields] {
    override def page = domainRecords
  }

  //Digital ocean is providing more limited information
  //about a droplet on creation, so just read the id.
  private [digitalocean] case class DropletCreation(
    droplet: Id,
    links: Links
  ) {
    def toDropletCreation(implicit client: digitalocean.DigitalOceanClient, ec: ExecutionContext): Future[digitalocean.DropletCreation] = {
      for {
        droplet <- digitalocean.Droplet(droplet.id)
      } yield {
        digitalocean.DropletCreation(
          droplet,
          links.actions.head.id
        )
      }
    }
  }

  private [digitalocean] case class Id(
    id: BigInt
  )

  private [digitalocean] case class Droplet(
    droplet: digitalocean.Droplet,
    links: Links
  )

  private [digitalocean] case class Droplets(
    droplets: Seq[digitalocean.Droplet],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Droplet] {
    override def page = droplets
  }

  private [digitalocean] case class Action(action: digitalocean.Action)

  private [digitalocean] case class Actions(
    actions: Seq[digitalocean.Action],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Action] {
    override def page = actions
  }

  private [digitalocean] case class Kernel(kernel: digitalocean.Kernel)

  private [digitalocean] case class Kernels(
    kernels: Seq[digitalocean.Kernel],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Kernel] {
    override def page = kernels
  }

  private [digitalocean] case class Snapshot(snapshot: digitalocean.Image)

  private [digitalocean] case class Snapshots(
    snapshots: Seq[digitalocean.Image],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Image] {
    override def page = snapshots
  }

  private [digitalocean] case class Backups(
    backups: Seq[digitalocean.Image],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Image] {
    override def page = backups
  }

  private [digitalocean] case class Image(image: digitalocean.Image)

  private [digitalocean] case class Images(
    images: Seq[digitalocean.Image],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Image] {
    override def page = images
  }

  private [digitalocean] case class Region(region: digitalocean.Region)

  private [digitalocean] case class Regions(
    regions: Seq[digitalocean.Region],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Region] {
    override def page = regions
  }

  private [digitalocean] case class Size(size: digitalocean.Size)

  private [digitalocean] case class Sizes(
    sizes: Seq[digitalocean.Size],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Size] {
    override def page = sizes
  }

  private [digitalocean] case class SshKey(sshKey: digitalocean.SshKey)

  private [digitalocean] case class SshKeys(
    sshKeys: Seq[digitalocean.SshKey],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.SshKey] {
    override def page = sshKeys
  }

  private [digitalocean] case class FloatingIp(
    floatingIp: digitalocean.FloatingIp
  )

  private [digitalocean] case class FloatingIps(
    floatingIps: Seq[digitalocean.FloatingIp],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.FloatingIp] {
    override def page = floatingIps
  }

  private [digitalocean] case class Volumes(
    volumes: Seq[digitalocean.Volume],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Volume] {
    override def page = volumes
  }

  private [digitalocean] case class Volume(
    volume: digitalocean.Volume
  )

  private [digitalocean] case class Firewall(
    id: String,
    status: digitalocean.Firewall.Status,
    createdAt: Instant,
    pendingChanges: Seq[digitalocean.Firewall.PendingChange],
    name: String,
    inboundRules: Seq[Firewall.InboundRule],
    outboundRules: Seq[Firewall.OutboundRule],
    dropletIds: Seq[BigInt],
    tags: Seq[String]
  ) {
    def toFirewall: digitalocean.Firewall =
      digitalocean.Firewall(
        id = id,
        status = status,
        createdAt = createdAt,
        pendingChanges = pendingChanges,
        name = name,
        inboundRules = inboundRules.map(_.toInboundRule),
        outboundRules = outboundRules.map(_.toOutboundRule),
        dropletIds = dropletIds,
        tags = tags
      )
  }

  private [digitalocean] object Firewall {

    def valueOf(firewall: digitalocean.Firewall): Firewall = {
      Firewall(
        id = firewall.id,
        status = firewall.status,
        createdAt = firewall.createdAt,
        pendingChanges = firewall.pendingChanges,
        name = firewall.name,
        inboundRules = firewall.inboundRules.map(Firewall.InboundRule.valueOf),
        outboundRules = firewall.outboundRules.map(Firewall.OutboundRule.valueOf),
        dropletIds = firewall.dropletIds,
        tags = firewall.tags
      )
    }

    case class InboundRule(
      protocol: Protocol,
      ports: Option[digitalocean.Firewall.Port],
      sources: digitalocean.Firewall.Source
    ) {
      def toInboundRule: digitalocean.Firewall.InboundRule = {
        val resultProtocol =
          protocol match {
            case Protocol.Icmp =>
              digitalocean.Firewall.Protocol.Icmp
            case Protocol.Tcp =>
              digitalocean.Firewall.Protocol.Tcp(ports.get)
            case Protocol.Udp =>
              digitalocean.Firewall.Protocol.Udp(ports.get)
          }
        digitalocean.Firewall.InboundRule(
          protocol = resultProtocol,
          sources = sources
        )
      }
    }

    object InboundRule {
      def valueOf(inboundRule: digitalocean.Firewall.InboundRule): InboundRule = {
        val (protocol, ports) =
          inboundRule.protocol match {
            case digitalocean.Firewall.Protocol.Icmp =>
              (Protocol.Icmp,  None)
            case digitalocean.Firewall.Protocol.Tcp(port) =>
              (Protocol.Tcp, Some(port))
            case digitalocean.Firewall.Protocol.Udp(port) =>
              (Protocol.Udp, Some(port))
          }

        InboundRule(
          protocol = protocol,
          ports = ports,
          sources = inboundRule.sources
        )
      }
    }

    case class OutboundRule(
      protocol: Protocol,
      ports: Option[digitalocean.Firewall.Port],
      destinations: digitalocean.Firewall.Destination
    ) {
      def toOutboundRule: digitalocean.Firewall.OutboundRule = {
        val resultProtocol =
          protocol match {
            case Protocol.Icmp =>
              digitalocean.Firewall.Protocol.Icmp
            case Protocol.Tcp =>
              digitalocean.Firewall.Protocol.Tcp(ports.get)
            case Protocol.Udp =>
              digitalocean.Firewall.Protocol.Udp(ports.get)
          }

        digitalocean.Firewall.OutboundRule(
          protocol = resultProtocol,
          destinations = destinations
        )
      }
    }

    object OutboundRule {
      def valueOf(outboundRule: digitalocean.Firewall.OutboundRule): OutboundRule = {
        val (protocol, ports) =
          outboundRule.protocol match {
            case digitalocean.Firewall.Protocol.Icmp =>
              (Protocol.Icmp,  None)
            case digitalocean.Firewall.Protocol.Tcp(port) =>
              (Protocol.Tcp, Some(port))
            case digitalocean.Firewall.Protocol.Udp(port) =>
              (Protocol.Udp, Some(port))
          }

        OutboundRule(
          protocol = protocol,
          ports = ports,
          destinations = outboundRule.destinations
        )
      }
    }

    sealed trait Protocol {
      val StringValue: String
    }

    object Protocol {

      case object Tcp extends Protocol {
        val StringValue = "tcp"
      }

      case object Udp extends Protocol {
        val StringValue = "udp"
      }

      case object Icmp extends Protocol {
        val StringValue = "icmp"
      }

      private[digitalocean] case object Serializer extends CustomSerializer[Protocol](format =>
        (
          {
            case JString(Tcp.StringValue) => Tcp
            case JString(Udp.StringValue) => Udp
            case JString(Icmp.StringValue) => Icmp
          },
          {
            case tpe: Protocol =>
              JString(tpe.StringValue)
          }
        )
      )

    }

    private[digitalocean] case class Source(
      addresses: Option[Seq[digitalocean.Firewall.Source.Address]],
      dropletIds: Option[Seq[BigInt]],
      loadBalancerUids: Option[Seq[String]],
      tags: Option[Seq[String]]
    ) {
      def toSource: digitalocean.Firewall.Source =
        digitalocean.Firewall.Source(
          addresses = addresses.getOrElse(Seq()),
          dropletIds = dropletIds.getOrElse(Seq()),
          loadBalancerUids = loadBalancerUids.getOrElse(Seq()),
          tags = tags.getOrElse(Seq())
        )
    }

    private[digitalocean] object Source {
      def valueOf(source: digitalocean.Firewall.Source): Source =
        Source(
          addresses = seqToOption(source.addresses),
          dropletIds = seqToOption(source.dropletIds),
          loadBalancerUids = seqToOption(source.loadBalancerUids),
          tags = seqToOption(source.tags)
        )
    }

  }

  private[digitalocean] case class Firewalls(
    firewalls: Seq[Firewall],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Firewall] {
    override def page: Seq[digitalocean.Firewall] = firewalls.map(_.toFirewall)
  }

  private[digitalocean] case class FirewallCreateOrUpdate(
    firewall: Firewall
  )

  private[digitalocean] case class Tag(
    tag: digitalocean.Tag
  )

  private [digitalocean] case class Tags(
    tags: Seq[digitalocean.Tag]
  )

}

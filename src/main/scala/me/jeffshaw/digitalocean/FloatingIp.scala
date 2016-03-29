package me.jeffshaw.digitalocean

import java.net.Inet4Address
import org.json4s.JsonDSL._
import scala.concurrent.{ExecutionContext, Future}

case class FloatingIp(
  ip: Inet4Address,
  region: Region,
  droplet: Option[Droplet],
  locked: Boolean
) {
  def delete()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    FloatingIp.delete(ip)
  }
}

object FloatingIp
  extends Path
  with Listable[FloatingIp, responses.FloatingIps] {

  override protected val path: Seq[String] = Seq("floating_ips")

  def apply(ip: Inet4Address)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[FloatingIp] = {
    val path = this.path :+ ip.getHostAddress
    client.get[FloatingIp](path)
  }

  def delete(ip: Inet4Address)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    val path = this.path :+ ip.getHostAddress
    client.delete(path)
  }

  def create(dropletId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[FloatingIp] = {
    client.post[responses.FloatingIp](path, ("droplet_id" -> dropletId)).map(_.floatingIp)
  }

  def create(region: RegionEnum)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[FloatingIp] = {
    client.post[responses.FloatingIp](path, ("region" -> region.slug)).map(_.floatingIp)
  }

}

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

  def assign(dropletId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    FloatingIp.assign(ip, dropletId)
  }

  def assign(droplet: Droplet)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    FloatingIp.assign(ip, droplet.id)
  }

  def unassign()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    FloatingIp.unassign(ip)
  }
}

object FloatingIp
  extends Path
  with Listable[FloatingIp, responses.FloatingIps] {

  override protected val path: Seq[String] = Seq("floating_ips")

  def apply(ip: Inet4Address)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[FloatingIp] = {
    val path = this.path :+ ip.getHostAddress
    client.get[responses.FloatingIp](path).map(_.floatingIp)
  }

  def assign(address: Inet4Address, dropletId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    val path = this.path :+ address.getHostAddress :+ "actions"
    client.post[responses.Action](path, ("type" -> "assign") ~ ("droplet_id" -> dropletId)).map(_.action)
  }

  def unassign(address: Inet4Address)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    val path = this.path :+ address.getHostAddress :+ "actions"
    client.post[responses.Action](path, "type" -> "unassign").map(_.action)
  }

  def delete(ip: Inet4Address)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    val path = this.path :+ ip.getHostAddress
    client.delete(path)
  }

  def create(dropletId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[FloatingIpCreation] = {
    //we need the droplet's region
    for {
      droplet <- Droplet(dropletId)
      floatingIp <- create(droplet)
    } yield floatingIp
  }

  def create(droplet: Droplet)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[FloatingIpCreation] = {
    /*
    When creating a FloatingIp directly onto a droplet, DO doesn't return the action.
    So instead, we create it, and then assign it, which gives us the action, so we can
    directly poll to know when it's done.
     */
    for {
      //create the floating ip in the droplet's region
      ip <- create(droplet.region.toEnum)
      //assign the ip to the droplet
      action <- ip.assign(droplet.id)
    } yield FloatingIpCreation(ip, action)
  }

  def create(region: RegionEnum)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[FloatingIp] = {
    client.post[responses.FloatingIp](path, ("region" -> region.slug)).map(_.floatingIp)
  }

}

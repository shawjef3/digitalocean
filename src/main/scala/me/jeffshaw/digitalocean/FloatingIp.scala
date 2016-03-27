package me.jeffshaw.digitalocean

import java.net.InetAddress

import scala.concurrent.{ExecutionContext, Future}

case class FloatingIp(
  ip: InetAddress,
  region: Region,
  droplet: Option[Droplet]
) {

}

object FloatingIp
  extends Path
  with Listable[FloatingIp, responses.FloatingIps] {
    self =>

  override protected val path: Seq[String] = Seq("floating_ips")

  def apply(id: InetAddress)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[FloatingIp] = {
    val path = self.path :+ id.getHostAddress
    client.get[FloatingIp](path: _*)
  }

}

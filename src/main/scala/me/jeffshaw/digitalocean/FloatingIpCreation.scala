package me.jeffshaw.digitalocean

import scala.concurrent.{ExecutionContext, Future}

case class FloatingIpCreation(
  floatingIp: FloatingIp,
  action: Action
) {
  def complete()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] =
    action.complete()
}

object FloatingIpCreation {
  implicit def asFloatingIp(ip: FloatingIpCreation): FloatingIp =
    ip.floatingIp
}

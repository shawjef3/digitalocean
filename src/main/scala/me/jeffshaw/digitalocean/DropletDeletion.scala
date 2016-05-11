package me.jeffshaw.digitalocean

import scala.concurrent._

/**
 * Droplet deletion doesn't return an action, so to know it's done we have to
 * check for the droplet's existence.
 * @param dropletId
 */
case class DropletDeletion(dropletId: BigInt) {
  def complete()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    client.poll[Boolean](Droplet.isDeleted(dropletId), identity).map(Function.const(()))
  }
}

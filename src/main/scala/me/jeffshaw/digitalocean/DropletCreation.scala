package me.jeffshaw.digitalocean

import scala.concurrent._

case class DropletCreation(
  droplet: Droplet,
  actionId: BigInt
) {
  def action(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Action(actionId)
  }

  def complete(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    for {
      a <- action
      c <- a.complete
    } yield c
  }
}

package me.jeffshaw.digitalocean

import scala.concurrent._

case class ActionRef(
  id: BigInt,
  rel: String,
  href: String
) {
  def action(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Action(id)
  }
}

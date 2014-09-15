package me.jeffshaw.digitalocean

import scala.concurrent._

case class DropletCreation(
  droplet: Droplet,
  actionId: BigInt
) {
  def action(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Action(actionId)
  }

  def isCompleted(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Boolean] = {
    action.map(_.isCompleted)
  }

  def await(implicit client: DigitalOceanClient, ec: ExecutionContext): Unit = {
    def completionCheck: Boolean = {
      Thread.sleep(client.actionCheckInterval.toMillis)
      Await.result(isCompleted, client.maxWaitPerRequest)
    }

    Iterator.continually(completionCheck).contains(true)
  }
}

package me.jeffshaw.digitalocean

import java.time.Instant

import scala.concurrent._

case class Action(
  id: BigInt,
  status: Action.Status,
  `type`: String,
  startedAt: Instant,
  completedAt: Option[Instant],
  resourceId: BigInt,
  resourceType: Action.ResourceType,
  region: String
) {

  def isCompleted: Boolean = {
    status != Action.InProgress
  }

  def await(implicit client: DigitalOceanClient, ec: ExecutionContext): Action = {
    def actionRefresh: Action = {
      Thread.sleep(client.actionCheckInterval.toMillis)
      Await.result(Action(id), client.maxWaitPerRequest)
    }

    Iterator.continually(actionRefresh).find(_.isCompleted)
  }.get
}

object Action extends Path with Listable[Action, responses.Actions] {
  override val path = Seq("actions")

  def apply(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    val path = this.path :+ id.toString
    for {
      response <- client.get[responses.Action](path: _*)
    } yield {
      response.action
    }
  }

  sealed trait ResourceType

  case object Droplet extends ResourceType

  case object Image extends ResourceType

  case object Backend extends ResourceType

  sealed trait Status

  case object InProgress extends Status

  case object Completed extends Status

  case object Errored extends Status
}

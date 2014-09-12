package me.jeffshaw.digitalocean

import java.time.Instant

import scala.concurrent.{ExecutionContext, Future}

case class Action(
  id: BigInt,
  status: String,
  `type`: String,
  startedAt: Instant,
  completedAt: Option[Instant],
  resourceId: BigInt,
  resourceType: Action.ResourceType,
  region: String
)

object Action {
  def apply(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    for {
      response <- client.get[responses.Action]("action", id.toString())
    } yield {
      response.action
    }
  }

  def list(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Action]] = {
    for {
      response <- client.get[me.jeffshaw.digitalocean.responses.Actions]("action")
    } yield {
      response.actions
    }
  }

  sealed trait ResourceType

  case object Droplet extends ResourceType

  case object Image extends ResourceType

  case object Backend extends ResourceType
}

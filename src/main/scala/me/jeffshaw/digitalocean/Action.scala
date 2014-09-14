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

}

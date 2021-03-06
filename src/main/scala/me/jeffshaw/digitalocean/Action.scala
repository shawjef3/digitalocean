package me.jeffshaw.digitalocean

import java.time.Instant

import scala.concurrent._

case class Action(
  id: BigInt,
  status: Action.Status,
  `type`: String,
  startedAt: Instant,
  completedAt: Option[Instant],
  resourceId: Option[BigInt],
  resourceType: Action.ResourceType,
  region: Region
) {

  def isCompleted: Boolean = {
    status != Action.InProgress
  }

  def refresh()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] =
    Action(id)

  /**
   * Polls the action for a completed or errored status, returning the final action value.
   * You can use Future.onError to set a callback if the final state is Errored, or
   * onSuccess if the final status is Completed.
   *
   * @param client
   * @param ec
   * @return
   */
  def complete()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    for {
      result <- client.poll[Action](Action(id), _.status != Action.InProgress)
    } yield {
      if (result.status == Action.Errored) throw new ActionErroredException(result)
      else result
    }
  }

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case that: Action =>
        eq(that) || this.id == that.id
      case _ =>
        false
    }
  }

  override def hashCode(): Int = {
    id.hashCode()
  }

}

object Action extends Path with Listable[Action, responses.Actions] {
  override val path = Seq("actions")

  def apply(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    val path = this.path :+ id.toString
    for {
      response <- client.get[responses.Action](path)
    } yield {
      response.action
    }
  }

  sealed trait ResourceType

  case object Droplet extends ResourceType

  case object Image extends ResourceType

  case object Backend extends ResourceType

  case object FloatingIp extends ResourceType

  sealed trait Status

  case object InProgress extends Status

  case object Completed extends Status

  case object Errored extends Status
}

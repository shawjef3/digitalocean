package me.jeffshaw.digitalocean

import java.time.Instant
import me.jeffshaw.digitalocean.Action.{InProgress, Errored, Completed}

import scala.concurrent._

case class Action(
  id: BigInt,
  status: Action.Status,
  `type`: String,
  startedAt: Instant,
  completedAt: Option[Instant],
  resourceId: BigInt,
  resourceType: Action.ResourceType,
  region: Region
) {

  def isCompleted: Boolean = {
    status != Action.InProgress
  }

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

    import DelayedFuture._

    def loop(): Future[Action] = {
      val timeout = client.actionCheckInterval + client.maxWaitPerRequest
      val whenAction = after(client.actionCheckInterval)(Action(id))
      val whenTimeout = after(timeout)(Future.failed(new TimeoutException()))
      val whenActionWithoutTimeout = Future.firstCompletedOf(Seq(whenAction, whenTimeout))

      whenActionWithoutTimeout.flatMap { action =>
        action.status match {
          case InProgress => loop()
          case Completed  => Future.successful(action)
          case Errored    => Future.failed(new ActionErroredException(action))
        }
      }
    }

    loop()
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

  sealed trait Status

  case object InProgress extends Status

  case object Completed extends Status

  case object Errored extends Status
}

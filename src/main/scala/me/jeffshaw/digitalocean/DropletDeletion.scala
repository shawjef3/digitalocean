package me.jeffshaw.digitalocean

import scala.concurrent._

/**
 * Droplet deletion doesn't return an action, so to know it's done we have to
 * check for the droplet's existence.
 * @param dropletId
 */
case class DropletDeletion(dropletId: BigInt) {
  def complete()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {

    import DelayedFuture._

    def existenceCheck(): Future[Unit] = {
      val timeout = client.actionCheckInterval + client.maxWaitPerRequest
      val whenTimeout = after(timeout)(Future.failed(new TimeoutException()))
      val whenDelete = after(client.actionCheckInterval)(Droplet.isDeleted(dropletId))
      val whenDeleteWithoutTimeout = Future.firstCompletedOf(Seq(whenDelete, whenTimeout))

      whenDeleteWithoutTimeout.flatMap {
        case isDeleted if isDeleted => Future.successful(())
        case _                      => existenceCheck()
      }
    }

    existenceCheck()
  }
}

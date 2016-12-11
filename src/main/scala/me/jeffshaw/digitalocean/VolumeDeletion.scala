package me.jeffshaw.digitalocean

import scala.concurrent.{ExecutionContext, Future}

case class VolumeDeletion(
  volumeId: String
) {
  def complete()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    client.poll[Boolean](Volume.isDeleted(volumeId), identity).map(Function.const(()))
  }
}

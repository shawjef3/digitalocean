package me.jeffshaw.digitalocean

import java.time.Instant

import scala.concurrent.{ExecutionContext, Future}

case class Image(
  id: BigInt,
  name: String,
  distribution: String,
  slug: Option[String],
  public: Boolean,
  regions: Seq[String],
  createdAt: Instant
) {
  def delete(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    Image.delete(id)
  }

  def restore(dropletId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.restore(dropletId, this)
  }

  def restore(droplet: Droplet)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    droplet.restore(this)
  }
}

object Image {
  def apply(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Image] = {
    apply(id.toString)
  }

  def apply(slug: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Image] = {
    for {
      response <- client.get[me.jeffshaw.digitalocean.responses.Image]("images", slug)
    } yield {
      response.image
    }
  }

  def list(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Image]] = {
    for {
      response <- client.get[me.jeffshaw.digitalocean.responses.Images]("images")
    } yield {
      response.images
    }
  }

  def delete(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    client.delete("images", id.toString)
  }
}

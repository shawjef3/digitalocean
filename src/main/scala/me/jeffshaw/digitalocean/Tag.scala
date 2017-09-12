package me.jeffshaw.digitalocean

import org.json4s.JsonDSL.WithBigDecimal._
import scala.concurrent.{ExecutionContext, Future}

case class Tag(
  name: String,
  resources: Tag.Resources
) {
  def delete()(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Tag.delete(name)
  }

  def tagDropletIds(
    droplets: Seq[BigInt]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Tag.tagDropletIds(name, droplets)
  }

  def tagDroplets(
    droplets: Seq[Droplet]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Tag.tagDroplets(name, droplets)
  }

  def untagDropletIds(
    droplets: Seq[BigInt]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Tag.untagDropletIds(name, droplets)
  }

  def untagDroplets(
    droplets: Seq[Droplet]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    Tag.untagDroplets(name, droplets)
  }
}

object Tag extends Path {

  override protected val path: Seq[String] = Seq("tags")

  def apply(
    name: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Tag] = {
    client.get[responses.Tag](path :+ name).map(_.tag)
  }

  def list()(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Seq[Tag]] = {
    client.get[responses.Tags](path).map(_.tags)
  }

  def create(
    name: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Tag] = {
    client.post[responses.Tag](path, "name" -> name).map(_.tag)
  }

  def delete(
    name: String
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    client.delete(path :+ name)
  }

  def tagDroplets(
    name: String,
    droplets: Seq[Droplet]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    tagDropletIds(name, droplets.map(_.id))
  }

  def tagDropletIds(
    name: String,
    dropletIds: Seq[BigInt]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    if (dropletIds.isEmpty) {
      Future.successful(())
    } else {
      val postBody =
        "resources" -> (
          for {
            id <- dropletIds
          } yield ("resource_id" -> id) ~ ("resource_type" -> "droplet")
        )
      client.postWithEmptyResponse(path :+ name, postBody)
    }
  }

  def untagDroplets(
    name: String,
    droplets: Seq[Droplet]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    untagDropletIds(name, droplets.map(_.id))
  }

  def untagDropletIds(
    name: String,
    dropletIds: Seq[BigInt]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[Unit] = {
    if (dropletIds.isEmpty) {
      Future.successful(())
    } else {
      val postBody =
        "resources" -> (
          for {
            id <- dropletIds
          } yield ("resource_id" -> id) ~ ("resource_type" -> "droplet")
        )
      client.delete(path :+ name, maybeMessage = Some(postBody))
    }
  }

  case class Resources(
    droplets: Resources.Droplets
  )

  object Resources {
    case class Droplets(
      count: BigInt,
      lastTagged: Option[Droplet]
    )
  }

}

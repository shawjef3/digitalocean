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
}

object Image
  extends Path
  with Listable[Image, responses.Images] {

  override val path: Seq[String] = Seq("images")

  def apply(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Image] = {
    apply(id.toString)
  }

  def apply(slug: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Image] = {
    val path = this.path :+ slug
    for {
      response <- client.get[responses.Image](path: _*)
    } yield {
      response.image
    }
  }

  def delete(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    val path = this.path :+ id.toString
    client.delete(path: _*)
  }
}

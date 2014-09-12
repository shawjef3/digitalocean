package me.jeffshaw.digitalocean

import scala.concurrent.{ExecutionContext, Future}

case class Region(
  slug: String,
  name: String,
  sizes: Seq[String],
  available: Boolean,
  features: Seq[String]
) {
  def getSizes(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Size]] = {
    Future.sequence(sizes.map(Size.apply))
  }

  def asEnum: RegionEnum = {
    RegionEnum.fromSlug(this.slug)
  }
}

object Region {
  /*
  /region/$slug isn't supported, so just get all of them and get the one we want from the list.
   */
  def apply(slug: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Region] = {
    for {
      response <- list
    } yield {
      response.find(_.slug == slug).getOrElse(throw new NoSuchElementException())
    }
  }

  def apply(enum: RegionEnum)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Region] = {
    apply(enum.slug)
  }

  def list(implicit client: DigitalOceanClient, ex: ExecutionContext): Future[Seq[Region]] = {
    for {
      response <- client.get[me.jeffshaw.digitalocean.responses.Regions]("regions")
    } yield {
      response.regions
    }
  }
}

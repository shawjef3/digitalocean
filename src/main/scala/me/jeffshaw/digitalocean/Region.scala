package me.jeffshaw.digitalocean

import scala.concurrent.{ExecutionContext, Future}

case class Region(
  slug: String,
  name: String,
  sizes: Seq[String],
  available: Option[Boolean],
  features: Seq[String]
) {
  def getSizes(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Size]] = {
    Future.sequence(sizes.map(Size.apply))
  }

  def toEnum: RegionEnum = {
    RegionEnum.fromSlug(this.slug)
  }
}

object Region
  extends Path
  with Listable[Region, responses.Regions] {

  override val path: Seq[String] = Seq("regions")

  /*
  /region/$slug isn't supported, so just get all of them and get the one we want from the list.
   */
  def apply(slug: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Region] = {
    for {
      response <- list
    } yield {
      response.find(_.slug == slug).getOrElse(throw new NoSuchElementException(slug))
    }
  }

  def apply(enum: RegionEnum)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Region] = {
    apply(enum.slug)
  }
}

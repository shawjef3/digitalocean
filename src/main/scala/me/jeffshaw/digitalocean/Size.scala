package me.jeffshaw.digitalocean

import scala.concurrent._

case class Size(
  slug: String,
  memory: Option[BigInt],
  vcpus: Option[BigInt],
  disk: Option[BigInt],
  transfer: BigInt,
  priceMonthly: BigDecimal,
  priceHourly: BigDecimal,
  regions: Seq[String]
) {
  def getRegions(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Region]] = {
    Future.sequence(regions.map(Region.apply))
  }

  def asEnum: SizeEnum = {
    SizeEnum.fromSlug(slug)
  }
}

object Size {
  /*
  /size/$name isn't supported, so just get all of them and get the one we want from the list.
   */
  def apply(slug: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Size] = {
    for {
      sizes <- list
    } yield {
      sizes.find(_.slug == slug).getOrElse(throw new NoSuchElementException())
    }
  }

  def list(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Size]] = {
    for {
      sizes <- client.get[me.jeffshaw.digitalocean.responses.Sizes]("sizes")
    } yield {
      sizes.sizes
    }
  }
}

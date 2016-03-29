package me.jeffshaw.digitalocean

import scala.concurrent._

case class Size(
  slug: String,
  memory: Option[BigInt],
  vcpus: Option[BigInt],
  disk: Option[BigInt],
  transfer: BigDecimal,
  priceMonthly: BigDecimal,
  priceHourly: BigDecimal,
  regions: Seq[String]
) {
  def getRegions(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Region]] = {
    Future.sequence(regions.map(Region.apply))
  }

  def toEnum: SizeEnum = {
    SizeEnum.fromSlug(slug)
  }
}

object Size
  extends Path
  with Listable[Size, responses.Sizes] {

  override val path: Seq[String] = Seq("sizes")

  /*
  /size/$name isn't supported, so just get all of them and get the one we want from the list.
   */
  def apply(slug: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Size] = {
    for {
      sizes <- list()
    } yield {
      sizes.find(_.slug == slug).getOrElse(throw new NoSuchElementException(slug))
    }
  }
}

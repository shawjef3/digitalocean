package me.jeffshaw.digitalocean

import scala.concurrent._, duration._

case class DigitalOcean(
  droplets: Iterator[Droplet],
  images: Iterator[Image],
  regions: Iterator[Region],
  sizes: Iterator[Size]
)

object DigitalOcean {
  def list(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[DigitalOcean] = {
    list(client.maxWaitPerPage)
  }

  def list(maxWaitPerPage: Duration)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[DigitalOcean] = {
    for {
      droplets <- Droplet.list(maxWaitPerPage: Duration)
      images <- Image.list(maxWaitPerPage: Duration)
      regions <- Region.list(maxWaitPerPage: Duration)
      sizes <- Size.list(maxWaitPerPage: Duration)
    } yield {
      DigitalOcean(droplets, images, regions, sizes)
    }
  }
}

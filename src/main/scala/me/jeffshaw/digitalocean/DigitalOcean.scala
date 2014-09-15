package me.jeffshaw.digitalocean

import scala.concurrent._

case class DigitalOcean(
  droplets: Iterator[Droplet],
  images: Iterator[Image],
  regions: Iterator[Region],
  sizes: Iterator[Size]
)

object DigitalOcean {
  def list(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[DigitalOcean] = {
    for {
      droplets <- Droplet.list
      images <- Image.list
      regions <- Region.list
      sizes <- Size.list
    } yield {
      DigitalOcean(droplets, images, regions, sizes)
    }
  }
}

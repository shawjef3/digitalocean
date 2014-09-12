package me.jeffshaw.digitalocean

import scala.concurrent.{ExecutionContext, Future}

case class DigitalOcean(droplets: Seq[Droplet],
                        images: Seq[Image],
                        regions: Seq[Region],
                        sizes: Seq[Size]
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

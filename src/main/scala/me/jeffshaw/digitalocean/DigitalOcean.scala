package me.jeffshaw.digitalocean

import scala.concurrent._

/**
  * Provides a snapshot of your Digital Ocean assets.
  */
case class DigitalOcean(
  domains: Set[dns.Domain],
  droplets: Set[Droplet],
  firewalls: Set[Firewall],
  floatingIps: Set[FloatingIp],
  images: Set[Image],
  regions: Set[Region],
  sizes: Set[Size],
  sshKeys: Set[SshKey],
  tags: Set[Tag],
  volumes: Set[Volume]
)

object DigitalOcean {
  def list()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[DigitalOcean] = {
    for {
      domains <- dns.Domain.list()
      droplets <- Droplet.list()
      firewalls <- Firewall.list()
      floatingIps <- FloatingIp.list()
      images <- Image.list()
      regions <- Region.list()
      sizes <- Size.list()
      sshKeys <- SshKey.list()
      tags <- Tag.list()
      volumes <- Volume.list()
    } yield {
      DigitalOcean(
        domains = domains.toSet,
        droplets = droplets.toSet,
        firewalls = firewalls.toSet,
        floatingIps = floatingIps.toSet,
        images = images.toSet,
        regions = regions.toSet,
        sizes = sizes.toSet,
        sshKeys = sshKeys.toSet,
        tags = tags.toSet,
        volumes = volumes.toSet
      )
    }
  }
}

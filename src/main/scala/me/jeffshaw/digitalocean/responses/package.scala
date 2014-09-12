package me.jeffshaw
package digitalocean

package object responses {

  case class Droplet(droplet: digitalocean.Droplet)

  case class Droplets(
    droplets: Seq[digitalocean.Droplet],
    meta: Option[Meta],
    links: Option[Links]
  )

  case class Action(action: digitalocean.Action)

  case class Actions(
    actions: Seq[digitalocean.Action],
    meta: Option[Meta],
    links: Option[Links]
  )

  case class Kernel(kernel: digitalocean.Kernel)

  case class Kernels(
    kernels: Seq[digitalocean.Kernel],
    meta: Option[Meta],
    links: Option[Links]
  )

  case class Snapshot(snapshot: digitalocean.Snapshot)

  case class Snapshots(
    snapshots: Seq[digitalocean.Snapshot],
    meta: Option[Meta],
    links: Option[Links]
  )

  case class Backups(
    backups: Seq[digitalocean.Image],
    meta: Option[Meta],
    links: Option[Links]
  )

  case class Image(image: digitalocean.Image)

  case class Images(
    images: Seq[digitalocean.Image],
    meta: Option[Meta],
    links: Option[Links]
  )

  case class Region(region: digitalocean.Region)

  case class Regions(
    regions: Seq[digitalocean.Region],
    meta: Option[Meta],
    links: Option[Links]
  )

  case class Size(size: digitalocean.Size)

  case class Sizes(
    sizes: Seq[digitalocean.Size],
    meta: Option[Meta],
    links: Option[Links]
  )

}

package me.jeffshaw
package digitalocean

package object responses {

  sealed trait Page[T] {
    def page: Seq[T]

    val meta: Option[Meta]
    val links: Option[Links]

    def size: Option[BigInt] = {
      meta.map(_.total)
    }
  }

  case class Droplet(
    droplet: digitalocean.Droplet,
    links: Links
  )

  case class Droplets(
    droplets: Seq[digitalocean.Droplet],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Droplet] {
    override val page = droplets
  }

  case class Action(action: digitalocean.Action)

  case class Actions(
    actions: Seq[digitalocean.Action],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Action] {
    override val page = actions
  }

  case class Kernel(kernel: digitalocean.Kernel)

  case class Kernels(
    kernels: Seq[digitalocean.Kernel],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Kernel] {
    override val page = kernels
  }

  case class Snapshot(snapshot: digitalocean.Image)

  case class Snapshots(
    snapshots: Seq[digitalocean.Image],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Image] {
    override val page = snapshots
  }

  case class Backups(
    backups: Seq[digitalocean.Image],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Image] {
    override val page = backups
  }

  case class Image(image: digitalocean.Image)

  case class Images(
    images: Seq[digitalocean.Image],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Image] {
    override val page = images
  }

  case class Region(region: digitalocean.Region)

  case class Regions(
    regions: Seq[digitalocean.Region],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Region] {
    override val page = regions
  }

  case class Size(size: digitalocean.Size)

  case class Sizes(
    sizes: Seq[digitalocean.Size],
    meta: Option[Meta],
    links: Option[Links]
  ) extends Page[digitalocean.Size] {
    override val page = sizes
  }
}

package me.jeffshaw.digitalocean

sealed trait SizeEnum {
  val slug: String
}

case object `512mb` extends SizeEnum {
  override val slug: String = "512mb"
}

case object `1gb` extends SizeEnum {
  override val slug: String = "1gb"
}

case object `2gb` extends SizeEnum {
  override val slug: String = "2gb"
}

case object `4gb` extends SizeEnum {
  override val slug: String = "4gb"
}

case object `8gb` extends SizeEnum {
  override val slug: String = "8gb"
}

case object `16gb` extends SizeEnum {
  override val slug: String = "16gb"
}

case object `m-16gb` extends SizeEnum {
  override val slug: String = "m-16gb"
}

case object `32gb` extends SizeEnum {
  override val slug: String = "32gb"
}

case object `m-1vcpu-8gb` extends SizeEnum {
  override val slug: String = "m-1vcpu-8gb"
}

case object `m-32gb` extends SizeEnum {
  override val slug: String = "m-32gb"
}

case object `48gb` extends SizeEnum {
  override val slug: String = "48gb"
}

case object `m-48gb` extends SizeEnum {
  override val slug: String = "m-48gb"
}

case object `64gb` extends SizeEnum {
  override val slug: String = "64gb"
}

case object `m-64gb` extends SizeEnum {
  override val slug: String = "m-64gb"
}

case object `m-128gb` extends SizeEnum {
  override val slug: String = "m-128gb"
}

case object `m-224gb` extends SizeEnum {
  override val slug: String = "m-224gb"
}

case object `c-2` extends SizeEnum {
  override val slug = "c-2"
}

case object `c-4` extends SizeEnum {
  override val slug = "c-4"
}

case object `c-8` extends SizeEnum {
  override val slug = "c-8"
}

case object `c-16` extends SizeEnum {
  override val slug = "c-16"
}

case object `c-32` extends SizeEnum {
  override val slug = "c-32"
}

case object `s-1vcpu-3gb` extends SizeEnum {
  override val slug: String = "s-1vcpu-3gb"
}

case object `s-6vcpu-16gb` extends SizeEnum {
  override val slug: String = "s-6vcpu-16gb"
}

case object `s-1vcpu-2gb` extends SizeEnum {
  override val slug: String = "s-1vcpu-2gb"
}

case object `s-12vcpu-48gb` extends SizeEnum {
  override val slug: String = "s-12vcpu-48gb"
}

case object `s-2vcpu-2gb` extends SizeEnum {
  override val slug: String = "s-2vcpu-2gb"
}

case object `s-32vcpu-192gb` extends SizeEnum {
  override val slug: String = "s-32vcpu-192gb"
}

case object `s-20vcpu-96gb` extends SizeEnum {
  override val slug: String = "s-20vcpu-96gb"
}

case object `s-16vcpu-64gb` extends SizeEnum {
  override val slug: String = "s-16vcpu-64gb"
}

case object `s-3vcpu-1gb` extends SizeEnum {
  override val slug: String = "s-3vcpu-1gb"
}

case object `s-24vcpu-128gb` extends SizeEnum {
  override val slug: String = "s-24vcpu-128gb"
}

case object `s-1vcpu-1gb` extends SizeEnum {
  override val slug: String = "s-1vcpu-1gb"
}

case object `c-48` extends SizeEnum {
  override val slug: String = "c-48"
}

case object `s-4vcpu-8gb` extends SizeEnum {
  override val slug: String = "s-4vcpu-8gb"
}

case object `s-8vcpu-32gb` extends SizeEnum {
  override val slug: String = "s-8vcpu-32gb"
}

case object `s-2vcpu-4gb` extends SizeEnum {
  override val slug: String = "s-2vcpu-4gb"
}

case object `g-2vcpu-8gb` extends SizeEnum {
  override val slug: String = "g-2vcpu-8gb"
}
case object `gd-2vcpu-8gb` extends SizeEnum {
  override val slug: String = "gd-2vcpu-8gb"
}
case object `g-4vcpu-16gb` extends SizeEnum {
  override val slug: String = "g-4vcpu-16gb"
}
case object `gd-4vcpu-16gb` extends SizeEnum {
  override val slug: String = "gd-4vcpu-16gb"
}
case object `g-8vcpu-32gb` extends SizeEnum {
  override val slug: String = "g-8vcpu-32gb"
}
case object `gd-8vcpu-32gb` extends SizeEnum {
  override val slug: String = "gd-8vcpu-32gb"
}
case object `g-16vcpu-64gb` extends SizeEnum {
  override val slug: String = "g-16vcpu-64gb"
}
case object `gd-16vcpu-64gb` extends SizeEnum {
  override val slug: String = "gd-16vcpu-64gb"
}
case object `g-32vcpu-128gb` extends SizeEnum {
  override val slug: String = "g-32vcpu-128gb"
}
case object `gd-32vcpu-128gb` extends SizeEnum {
  override val slug: String = "gd-32vcpu-128gb"
}
case object `g-40vcpu-160gb` extends SizeEnum {
  override val slug: String = "g-40vcpu-160gb"
}
case object `gd-40vcpu-160gb` extends SizeEnum {
  override val slug: String = "gd-40vcpu-160gb"
}

case class OtherSize(slug: String) extends SizeEnum

object SizeEnum {
  implicit def fromSlug(slug: String): SizeEnum = {
    slugEnumMap.getOrElse(slug, OtherSize(slug))
  }

  implicit def fromSize(size: Size): SizeEnum = {
    size.toEnum
  }

  val slugEnumMap = Map(
    `512mb`.slug -> `512mb`,
    `1gb`.slug -> `1gb`,
    `2gb`.slug -> `2gb`,
    `4gb`.slug -> `4gb`,
    `8gb`.slug -> `8gb`,
    `16gb`.slug -> `16gb`,
    `m-16gb`.slug -> `m-16gb`,
    `32gb`.slug -> `32gb`,
    `m-1vcpu-8gb`.slug -> `m-1vcpu-8gb`,
    `m-32gb`.slug -> `m-32gb`,
    `48gb`.slug -> `48gb`,
    `64gb`.slug -> `64gb`,
    `m-64gb`.slug -> `m-64gb`,
    `m-128gb`.slug -> `m-128gb`,
    `m-224gb`.slug -> `m-224gb`,
    `c-2`.slug -> `c-2`,
    `c-4`.slug -> `c-4`,
    `c-8`.slug -> `c-8`,
    `c-16`.slug -> `c-16`,
    `c-32`.slug -> `c-32`,
    `s-1vcpu-3gb`.slug -> `s-1vcpu-3gb`,
    `s-6vcpu-16gb`.slug -> `s-6vcpu-16gb`,
    `s-1vcpu-2gb`.slug -> `s-1vcpu-2gb`,
    `s-12vcpu-48gb`.slug -> `s-12vcpu-48gb`,
    `s-2vcpu-2gb`.slug -> `s-2vcpu-2gb`,
    `s-32vcpu-192gb`.slug -> `s-32vcpu-192gb`,
    `s-20vcpu-96gb`.slug -> `s-20vcpu-96gb`,
    `s-16vcpu-64gb`.slug -> `s-16vcpu-64gb`,
    `s-3vcpu-1gb`.slug -> `s-3vcpu-1gb`,
    `s-24vcpu-128gb`.slug -> `s-24vcpu-128gb`,
    `s-1vcpu-1gb`.slug -> `s-1vcpu-1gb`,
    `c-48`.slug -> `c-48`,
    `s-4vcpu-8gb`.slug -> `s-4vcpu-8gb`,
    `s-8vcpu-32gb`.slug -> `s-8vcpu-32gb`,
    `s-2vcpu-4gb`.slug -> `s-2vcpu-4gb`,
    `g-2vcpu-8gb`.slug -> `g-2vcpu-8gb`,
    `gd-2vcpu-8gb`.slug -> `gd-2vcpu-8gb`,
    `g-4vcpu-16gb`.slug -> `g-4vcpu-16gb`,
    `gd-4vcpu-16gb`.slug -> `gd-4vcpu-16gb`,
    `g-8vcpu-32gb`.slug -> `g-8vcpu-32gb`,
    `gd-8vcpu-32gb`.slug -> `gd-8vcpu-32gb`,
    `g-16vcpu-64gb`.slug -> `g-16vcpu-64gb`,
    `gd-16vcpu-64gb`.slug -> `gd-16vcpu-64gb`,
    `g-32vcpu-128gb`.slug -> `g-32vcpu-128gb`,
    `gd-32vcpu-128gb`.slug -> `gd-32vcpu-128gb`,
    `g-40vcpu-160gb`.slug -> `g-40vcpu-160gb`,
    `gd-40vcpu-160gb`.slug -> `gd-40vcpu-160gb`
  )
}

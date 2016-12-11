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
    `m-32gb`.slug -> `m-32gb`,
    `48gb`.slug -> `48gb`,
    `64gb`.slug -> `64gb`,
    `m-64gb`.slug -> `m-64gb`,
    `m-128gb`.slug -> `m-128gb`,
    `m-224gb`.slug -> `m-224gb`
  )
}

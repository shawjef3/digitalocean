package me.jeffshaw.digitalocean

import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

sealed trait RegionEnum {
  val slug: String
}

case object NewYork1 extends RegionEnum {
  override val slug: String = "nyc1"
}

case object Amsterdam1 extends RegionEnum {
  override val slug: String = "ams1"
}

case object SanFrancisco1 extends RegionEnum {
  override val slug: String = "sfo1"
}

case object NewYork2 extends RegionEnum {
  override val slug: String = "nyc2"
}

case object Amsterdam2 extends RegionEnum {
  override val slug: String = "ams2"
}

case object Singapore1 extends RegionEnum {
  override val slug: String = "sgp1"
}

case object London1 extends RegionEnum {
  override val slug: String = "lon1"
}

case object NewYork3 extends RegionEnum {
  override val slug: String = "nyc3"
}

case object Amsterdam3 extends RegionEnum {
  override val slug: String = "ams3"
}

case object Frankfurt1 extends RegionEnum {
  override val slug: String = "fra1"
}

case object Toronto1 extends RegionEnum {
  override val slug: String = "tor1"
}

case object Bangalore1 extends RegionEnum {
  override val slug: String = "blr1"
}

case class OtherRegion(slug: String) extends RegionEnum

object RegionEnum {
  implicit def fromSlug(slug: String): RegionEnum = {
    slugEnumMap.getOrElse(slug, OtherRegion(slug))
  }

  implicit def fromRegion(r: Region): RegionEnum = {
    r.toEnum
  }

  val slugEnumMap = Map(
    NewYork1.slug -> NewYork1,
    Amsterdam1.slug -> Amsterdam1,
    SanFrancisco1.slug -> SanFrancisco1,
    NewYork2.slug -> NewYork2,
    Amsterdam2.slug -> Amsterdam2,
    Singapore1.slug -> Singapore1,
    London1.slug -> London1,
    NewYork3.slug -> NewYork3,
    Amsterdam3.slug -> Amsterdam3,
    Frankfurt1.slug -> Frankfurt1,
    Toronto1.slug -> Toronto1,
    Bangalore1.slug -> Bangalore1
  )

  private[digitalocean] case object Serializer extends CustomSerializer[RegionEnum](format =>
    (
      {
        case JString(slug) => fromSlug(slug)
      },
      {
        case regionEnum: RegionEnum =>
          JString(regionEnum.slug)
      }
    )
  )
}

package me.jeffshaw.digitalocean

import scala.concurrent._, duration._

class RegionSpec extends Spec {
  var cache: Option[Seq[Region]] = None

  def getCache: Seq[Region] = {
    val regions = cache.getOrElse(Await.result(Region.list, 5 seconds).toSeq)

    if (cache.isEmpty) {
      cache = Some(regions)
    }
    regions
  }

  test("Regions can be listed by the client") {
    val regions = Await.result(Region.list, 5 seconds).toSeq

    if (cache.isEmpty) {
      cache = Some(regions)
    }

    assert(regions.size > 0)
  }

  test("All regions are explicitly enumerated.") {
    val regions = getCache

    assert(regions.forall(r => ! r.toEnum.isInstanceOf[OtherRegion]))
  }
}

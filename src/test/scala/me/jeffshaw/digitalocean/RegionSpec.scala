package me.jeffshaw.digitalocean

import scala.concurrent._

class RegionSpec extends Spec {

  lazy val regions: Seq[Region] = {
    Await.result(Region.list, client.maxWaitPerRequest).toSeq
  }

  test("Regions can be listed by the client") {
    assert(regions.size > 0)
  }

  test("All regions are explicitly enumerated.") {
    for (region <- regions) {
      assert(! region.toEnum.isInstanceOf[OtherRegion])
    }
  }

}

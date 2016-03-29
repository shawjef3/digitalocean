package me.jeffshaw.digitalocean

import scala.concurrent._

class RegionSpec extends Suite {

  test("Regions can be listed by the client") {
    assert(Await.result(Region.size, client.maxWaitPerRequest) > 0)
  }

  test("All regions are explicitly enumerated.") {
    for (region <- Await.result(Region.list, client.maxWaitPerRequest).toSeq) {
      assert(! region.toEnum.isInstanceOf[OtherRegion])
    }
  }

}

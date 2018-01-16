package me.jeffshaw.digitalocean

class RegionSpec extends Suite {

  test("Regions can be listed by the client") {
    for (size <- Region.size()) yield {
      assert(size > 0)
    }
  }

  test("All regions are explicitly enumerated.") {
    for (regions <- Region.list()) yield {
      val asEnums = regions.map(_.toEnum)
      assert(asEnums.forall(!_.isInstanceOf[OtherRegion]))
    }
  }

}

package me.jeffshaw.digitalocean

import scala.concurrent._
import scala.concurrent.duration._

class SizeSpec extends Spec {
  var cache: Option[Seq[Size]] = None

  def getCache: Seq[Size] = {
    val regions = cache.getOrElse(Await.result(Size.list, 5 seconds))

    if (cache.isEmpty) {
      cache = Some(regions)
    }
    regions
  }

  test("Sizes can be listed by the client") {
    val sizes = Await.result(Size.list, 5 seconds)

    if (cache.isEmpty) {
      cache = Some(sizes)
    }

    assert(sizes.size > 0)
  }

  test("All sizes are explicitly enumerated.") {
    val sizes = getCache

    assert(sizes.forall(s => ! s.asEnum.isInstanceOf[OtherSize]))
  }
}

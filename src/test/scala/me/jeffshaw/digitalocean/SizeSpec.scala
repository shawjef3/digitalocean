package me.jeffshaw.digitalocean

import scala.concurrent._, duration._

class SizeSpec extends Suite {
  var cache: Option[Seq[Size]] = None

  def getCache: Seq[Size] = {
    val sizes = cache.getOrElse(Await.result(Size.list(), 5 seconds)).toSeq

    if (cache.isEmpty) {
      cache = Some(sizes)
    }
    sizes
  }

  test("Sizes can be listed by the client") {
    val sizes = Await.result(Size.list(), 5 seconds).toSeq

    if (cache.isEmpty) {
      cache = Some(sizes)
    }

    assert(sizes.nonEmpty)
  }

  test("All sizes are explicitly enumerated.") {
    val sizes = getCache

    val nonEnumSizes = sizes.map(_.toEnum).filter {
      case o: OtherSize =>
        true
      case _ =>
        false
    }

    assert(nonEnumSizes.isEmpty)
  }
}

package me.jeffshaw.digitalocean

import scala.concurrent._
import scala.concurrent.duration._

class ImageSpec extends Spec {
  test("Images can be listed by the client") {
    val images = Await.result(Image.list, 5 seconds)

    assert(images.size > 0)
  }
}

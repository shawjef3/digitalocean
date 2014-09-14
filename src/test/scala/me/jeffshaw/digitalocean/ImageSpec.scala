package me.jeffshaw.digitalocean

import scala.concurrent._, duration._

class ImageSpec extends Spec {
  test("Images can be listed by the client") {
    //This api call takes a little longer than others,
    //so let the test take a little longer.
    val images = Await.result(Image.list, 10 seconds)

    assert(images.hasNext)
  }

  test("More than a page of images can be listed by the client.") {
    val images = Await.result(Image.list(10 seconds), 1 minute)

    assert(images.size > 25)
  }
}

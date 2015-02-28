package me.jeffshaw.digitalocean

import scala.concurrent._, duration._

class ImageSpec extends Spec {
  test("Images can be listed by the client") {
    //This api call takes a little longer than others,
    //so let the test take a little longer.
    val images = Await.result(Image.list, 10 seconds)

    assert(images.hasNext)
  }

  //By default, the page size is 25,so try and list more images than that.
  //source: https://developers.digitalocean.com/documentation/v2/#links
  test("More than a page of images can be listed by the client.") {
    val images = Await.result(Image.list, 20 seconds)

    val imagesPrefix = images.take(26)

    assert(imagesPrefix.size > 25)
  }
}

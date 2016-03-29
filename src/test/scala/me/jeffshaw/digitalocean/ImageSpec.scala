package me.jeffshaw.digitalocean

import scala.concurrent._, duration._

class ImageSpec extends Suite {
  test("Images can be listed by the client") {
    //This api call takes a little longer than others,
    //so let the test take a little longer.
    val images = Await.result(Image.list(`private` = false, listType = Image.ListType.Distribution), 10 seconds).toList

    assert(images.exists(_.slug == Some(testImageSlug)))
  }

  //By default, the page size is 25,so try and list more images than that.
  //source: https://developers.digitalocean.com/documentation/v2/#links
  test("More than a page of images can be listed by the client.") {
    val images = Await.result(Image.list(`private` = false), 20 seconds)

    val imagesPrefix = images.take(26)

    assert(imagesPrefix.size > 25)
  }
}

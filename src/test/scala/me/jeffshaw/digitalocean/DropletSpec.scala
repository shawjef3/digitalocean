package me.jeffshaw.digitalocean

import scala.concurrent._, duration._

class DropletSpec extends Spec {
  test("Droplets can be listed") {
    val droplets = Await.result(Droplet.list, 5 seconds)

    assert(droplets.hasNext)
  }

  test("Droplets can be created and deleted.") {
    val name = "test"
    val region = NewYork2
    val size = `512mb`
    val image = 3448674 //CentOS 6.5 32-bit

    val droplet =
      Await.result(Droplet.create(name, region, size, image, Seq.empty, false, false, false, None), 10 seconds)

    //Wait for the droplet to be ready for use.
    droplet.await

    println(s"Droplet ${droplet.id} is active. Deleting it.")

    //Power it off (not necessary, but we don't have a test for Action.await yet.
    Await.result(droplet.powerOff, 5 seconds).await

    //Wait 5 seconds for the delete command to return, and then
    //wait for the droplet to stop existing.
    Await.result(droplet.delete, 5 seconds).await

    println("deletion completed")
  }
}

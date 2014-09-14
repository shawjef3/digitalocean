package me.jeffshaw.digitalocean

import scala.concurrent._
import scala.concurrent.duration._

class DropletSpec extends Spec {
  test("Droplets can be listed")
  {
    val droplets = Await.result(Droplet.list(5 seconds), 5 seconds)

    assert(droplets.hasNext)
  }

  def eventuallyFails[T](maxWait: Duration, f: => T): Boolean = {
    val failAfter = System.currentTimeMillis() + maxWait.toMillis
    try {
      while (System.currentTimeMillis() < failAfter) {
        f
        Thread.sleep(5 * 1000)
      }
      false
    } catch {
      case e: Exception =>
        true
    }
  }

  test("Droplets can be created and deleted.") {
    val name = "test"
    val region = NewYork2
    val size = `512mb`
    val image = 3448674 //CentOS 6.5 32-bit

    val droplet = Await.result(Droplet.create(name, region, size, image, Seq.empty, false, false, false, None), 10 seconds)

    while (Await.result(Droplet(droplet.id), 10 seconds).status != Active) {
      println(s"waiting for status = active for droplet ${droplet.id}")
      Thread.sleep(5 * 1000)
    }

    println("Waiting 10 seconds for things to settle down.")
    Thread.sleep(10 * 1000)

    println(s"deleting droplet ${droplet.id}")
    Await.result(droplet.delete, 10 seconds)

    assert(eventuallyFails(5 minutes, Await.result(Droplet(droplet.id), 5 seconds)))

    println("deletion completed")
  }
}

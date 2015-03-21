package me.jeffshaw.digitalocean

import org.scalatest.BeforeAndAfterAll

import scala.concurrent._, duration._
import scala.util.Random

class DropletSpec extends Spec with BeforeAndAfterAll {

  //This is used for clean-up in afterAll(), in case creation succeeds,
  //but something bad happens afterwards.
  private var dropletId: Option[BigInt] = None

  val dropletName = "ScalaTest" + Random.nextInt()

  test("Droplets can be created, listed, and deleted.") {
    val region = NewYork2
    val size = `512mb`
    val image = 10325992 //CentOS 6.5 32-bit

    val droplet =
      Await.result(Droplet.create(dropletName, region, size, image, Seq.empty, false, false, false, None), 10 seconds)

    dropletId = Some(droplet.id)

    //Now that the droplet is created, listing droplets should
    //yield a non-empty list.
    val droplets = Await.result(Droplet.list, 5 seconds)

    assert(droplets.hasNext)

    //Wait for the droplet to become active.
    Await.result(droplet.complete, 2 minutes)

    println(s"Droplet ${droplet.id} is active. Deleting it.")

    //Power it off (not necessary, but we don't have a test for Action.await yet.
    Await.result(droplet.powerOff.flatMap(_.complete), 1 minute)

    //Wait 5 seconds for the delete command to return, and then
    //wait for the droplet to stop existing.
    Await.result(droplet.delete.flatMap(_.complete), 1 minute)

    println("deletion completed")
  }

  override protected def afterAll(): Unit = {
    scala.util.Try(dropletId.foreach(Droplet.delete))
  }
}

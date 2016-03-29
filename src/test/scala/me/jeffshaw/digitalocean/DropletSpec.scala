package me.jeffshaw.digitalocean

import org.scalatest.BeforeAndAfterAll
import scala.concurrent._, duration._
import scala.util.Random

class DropletSpec extends Suite with BeforeAndAfterAll {

  val dropletNamePrefix = "ScalaTest"

  val dropletName = dropletNamePrefix + Random.nextInt()

  test("Droplets can be created, listed, and deleted.") {
    val size = `512mb`

    val t = for {
      droplet <- Droplet.create(dropletName, testRegionSlug, size, testImageSlug, Seq.empty, false, false, false, None)
      droplets <- Droplet.list()
      () = assert(droplets.exists(_.id == droplet.id))
      //Wait for the droplet to become active.
      _ <- droplet.complete()
      () = println(s"Droplet ${droplet.id} is active. Deleting it.")
      //Power it off (not necessary, but we don't have a test for Action.await yet).
      off <- droplet.powerOff()
      _ <- off.complete
      //Wait 5 seconds for the delete command to return, and then
      //wait for the droplet to stop existing.
      delete <- droplet.delete()
      () <- delete.complete()
    } yield println("deletion completed")

    Await.result(t, 5 minutes)
  }

  override protected def afterAll(): Unit = {
    val deletions = for {
      droplets <- Droplet.list()
      deletes <- Future.sequence(droplets.filter(_.name.startsWith(dropletNamePrefix)).map(_.delete))
    } yield deletes

    Await.result(deletions, 3 minutes)
  }
}

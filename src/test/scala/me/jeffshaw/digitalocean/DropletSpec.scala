package me.jeffshaw.digitalocean

import scala.concurrent._, duration._
import scala.util.Random

class DropletSpec extends Suite {

  val dropletName = dropletNamePrefix + Random.nextInt()

  test("Droplets can be created, listed, and deleted.") {
    val size = `512mb`

    val t = for {
      droplet <- Droplet.create(dropletName, testRegionSlug, size, testImageSlug, Seq.empty, false, false, false, None)
      droplets <- Droplet.list()
      _ = assert(droplets.exists(_.id == droplet.id))
      //Wait for the droplet to become active.
      createComplete <- droplet.complete()
      _ = assertResult(Action.Completed)(createComplete.status)
      () = println(s"Droplet ${droplet.id} is active. Deleting it.")
      //Power it off (not necessary, but we don't have a test for Action.await yet).
      off <- droplet.powerOff()
      offComplete <- off.complete()
      _ = assertResult(Action.Completed)(offComplete.status)
      //Wait for the droplet to stop existing.
      delete <- droplet.delete()
      () <- delete.complete()
      //Assert that the droplet has no pending actions.
      actions <- client.poll[Iterator[Action]](droplet.actions(), _.forall(_.status != Action.InProgress))
      //Assert that the droplet to stop appearing in the droplet list.
      droplets <- client.poll[Iterator[Droplet]](Droplet.list(), ! _.contains(droplet))
    } yield println("deletion completed")

    Await.result(t, 5 minutes)
  }

  override protected def afterAll(): Unit = {
    Await.result(deleteDroplets(), 3 minutes)
    super.afterAll()
  }
}

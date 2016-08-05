package me.jeffshaw.digitalocean

import org.scalatest.BeforeAndAfterAll
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.Random

class VolumeSpec
  extends Suite
    with BeforeAndAfterAll {

  test("volumes can be created and destroyed") {
    val volumeName = volumeNamePrefix + Random.nextInt()
    val r =
      for {
        v <- Volume.create(1, volumeName, None, NewYork1)
        () = assertResult(volumeName)(v.name)
        () = assertResult(1)(v.sizeGigabytes)
        () <- v.delete()
      } yield ()
    Await.result(r, 10 seconds)
  }

  test("volumes can be resized") {
    val volumeName = volumeNamePrefix + Random.nextInt()
    val r =
      for {
        v <- Volume.create(1, volumeName, None, NewYork1)
        resizeAction <- v.resize(2)
        v2 <- Volume(v.id)
        () = assertResult(2)(v2.sizeGigabytes)
        () <- v.delete()
      } yield ()
    Await.result(r, 10 seconds)
  }

  test("actions can be listed") {
    val volumeName = volumeNamePrefix + Random.nextInt()
    val dropletName = dropletNamePrefix + Random.nextInt()
    val size = `512mb`

    val r =
      for {
        v <- Volume.create(1, volumeName, None, NewYork1)
        d <- Droplet.create(dropletName, NewYork1, size, testImageSlug, Seq.empty, false, false, false, None)
        _ <- d.complete()
        action <- d.attach(v)
        actions <- v.actions()
        () = assert(actions.contains(action))
      } yield ()
    Await.result(r, 5 minutes)
  }

  override protected def afterAll(): Unit = {
    val deletes = for {
      volumes <- Volume.list()
      testVolumes = volumes.filter(_.name.startsWith(volumeNamePrefix))
      detaches <- Future.sequence(testVolumes.flatMap(v => v.dropletIds.map(v.detach)))
      _ <- Future.sequence(detaches.map(_.complete()))
      deletes <- Future.sequence(testVolumes.map(_.delete()))
    } yield ()

    Await.result(deletes, 1 minute)

    val deletions = for {
      droplets <- Droplet.list()
      deletes <- Future.sequence(droplets.filter(_.name.startsWith(dropletNamePrefix)).map(_.delete))
    } yield deletes

    Await.result(deletions, 3 minutes)
  }

}

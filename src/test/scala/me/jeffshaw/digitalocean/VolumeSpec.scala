package me.jeffshaw.digitalocean

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.Random

class VolumeSpec
  extends Suite {

  test("volumes can be created and destroyed") {
    val volumeName = volumeNamePrefix + Random.nextInt()
    val r =
      for {
        v <- Volume.create(1, volumeName, None, NewYork1)
        _ = assertResult(volumeName)(v.name)
        _ = assertResult(1)(v.sizeGigabytes)
        deletion <- v.delete()
        () <- deletion.complete()
      } yield ()
    Await.result(r, 10 seconds)
  }

  test("volumes can be resized") {
    val volumeName = volumeNamePrefix + Random.nextInt()
    val r =
      for {
        v <- Volume.create(1, volumeName, None, NewYork1)
        resizeAction <- v.resize(2)
        _ <- resizeAction.complete()
        v2 <- Volume(v.id)
      } yield assertResult(2)(v2.sizeGigabytes)
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
      } yield assert(actions.contains(action))
    Await.result(r, 5 minutes)
  }

  override protected def afterAll(): Unit = {
    val deleteVolumes = for {
      volumes <- Volume.list()
      testVolumes = volumes.toList.filter(_.name.startsWith(volumeNamePrefix))
      detaches <- Future.sequence(testVolumes.flatMap(v => v.dropletIds.map(v.detach)))
      _ <- Future.sequence(detaches.map(_.complete()))
      _ <- Future.sequence(testVolumes.map(_.delete().flatMap(_.complete())))
    } yield ()

    Await.result(deleteVolumes, 1 minute)
    Await.result(deleteDroplets(), 3 minutes)

    super.afterAll()
  }

}

package me.jeffshaw.digitalocean

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random

class VolumeSpec
  extends Suite {

  test("volumes can be created and destroyed") {
    val volumeName = volumeNamePrefix + Random.nextInt()
    for {
      v <- Volume.create(1, volumeName, None, NewYork1)
      deletion <- v.delete()
      () <- deletion.complete()
      volumes <- client.poll[Iterator[Volume]](Volume.list(), ! _.contains(v))
    } yield {
      assertResult(volumeName)(v.name)
      assertResult(1)(v.sizeGigabytes)
    }
  }

  test("volumes can be resized") {
    val volumeName = volumeNamePrefix + Random.nextInt()
    for {
      v <- Volume.create(1, volumeName, None, NewYork1)
      resizeAction <- v.resize(2)
      _ <- resizeAction.complete()
      v2 <- Volume(v.id)
    } yield assertResult(2)(v2.sizeGigabytes)
  }

  test("actions can be listed") {
    val volumeName = volumeNamePrefix + Random.nextInt()
    val dropletName = dropletNamePrefix + Random.nextInt()
    val size = `512mb`

    for {
      v <- Volume.create(1, volumeName, None, NewYork1)
      d <- Droplet.create(dropletName, NewYork1, size, testImageSlug, Seq.empty, false, false, false, None)
      _ <- d.complete()
      action <- d.attach(v)
      actions <- client.poll[Seq[Action]](v.actions().map(_.toList), _.contains(action))
    } yield succeed
  }

  override protected def afterAll(): Unit = {
    Await.result(deleteDroplets(), 3 minutes)

    val volumes =
      Await.result(listVolumes(), 3 minutes)

    for {
      volume <- volumes
    } yield util.Try(Await.result(volume.delete(), 20 seconds))

    super.afterAll()
  }

}

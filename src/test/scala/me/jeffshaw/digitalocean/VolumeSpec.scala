package me.jeffshaw.digitalocean

import org.scalatest.BeforeAndAfterAll
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random

class VolumeSpec extends Suite with BeforeAndAfterAll {

  val volumeNamePrefix = "scala-test-"

  val volumeName = volumeNamePrefix + Random.nextInt()

  test("volumes can be created and destroyed") {
    val r=
      for {
        v <- Volume.create(1, volumeName, None, NewYork1)
        () = assertResult(volumeName)(v.name)
        () = assertResult(1)(v.sizeGigabytes)
        () <- v.delete()
      } yield ()
    Await.result(r, 10 seconds)
  }

  test("volumes can be resized") {
    val r=
      for {
        v <- Volume.create(1, volumeName, None, NewYork1)
        resizeAction <- v.resize(2)
        v2 <- Volume(v.id)
        () = assertResult(2)(v2.sizeGigabytes)
        () <- v.delete()
      } yield ()
    Await.result(r, 10 seconds)
  }

}

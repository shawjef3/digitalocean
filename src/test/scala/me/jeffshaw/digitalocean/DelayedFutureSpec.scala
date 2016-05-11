package me.jeffshaw.digitalocean

import java.util.concurrent.atomic.AtomicBoolean
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class DelayedFutureSpec
  extends Suite
  with DelayedFuture {

  test("sleep sleeps") {
    val start = System.currentTimeMillis()
    val sleeping = sleep(1 second)
    Await.result(sleeping, 2 seconds)
    val end = System.currentTimeMillis()
    assert(end - start >= 1000L)
  }

  test("action is run") {
    val ref = new AtomicBoolean(false)
    after(1 second)(Future(ref.set(true)))
    Await.result(sleep(2 seconds), 3 seconds)
    assert(ref.get)
  }

  test("failure is passed through") {
    val failed = new AtomicBoolean(false)
    val delayed = after[Unit](1 second)(Future.failed(new Exception))
    delayed.onComplete[Unit](result => failed.set(result.isFailure))
    Await.ready(delayed, 2 seconds)
    assert(failed.get())
  }

}

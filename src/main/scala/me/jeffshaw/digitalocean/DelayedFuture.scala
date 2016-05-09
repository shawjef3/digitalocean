package me.jeffshaw.digitalocean

import java.util.{TimerTask, Timer}

import scala.concurrent.{Promise, Future}
import scala.concurrent.duration.Duration

object DelayedFuture {

  def after[T](duration: Duration)(future: => Future[T]) = {
    val p = Promise[T]()

    val timer = new Timer(true)
    timer.schedule(new TimerTask {
      override def run(): Unit = {
        p.completeWith(future)
        timer.cancel()
      }
    }, duration.toMillis)

    p.future
  }
}

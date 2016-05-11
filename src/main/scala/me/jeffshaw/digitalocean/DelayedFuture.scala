package me.jeffshaw.digitalocean

import java.io.Closeable
import java.util.{Timer, TimerTask}
import scala.concurrent.{Future, Promise}
import scala.concurrent.duration.Duration

/**
  * Provides methods to run functions after a delay.
  */
private[digitalocean] trait DelayedFuture
  extends Closeable {

  private val timer = new Timer(true)

  protected def after[T](
    delay: Duration
  )(future: => Future[T]
  ): Future[T] = {
    val p = Promise[T]()

    timer.schedule(
      new TimerTask {
        override def run(): Unit = {
          p.completeWith(future)
        }
      },
      delay.toMillis
    )

    p.future
  }

  protected def sleep(duration: Duration): Future[Unit] =
    after(duration)(Future.successful(()))

  override def close(): Unit = {
    timer.cancel()
  }

}

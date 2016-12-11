package me.jeffshaw.digitalocean

import java.util.concurrent.Executor
import org.asynchttpclient.ListenableFuture
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.control.NonFatal

private[digitalocean] object ToFuture {

  implicit def toFuture[A](lf: ListenableFuture[A])(implicit ec: ExecutionContext): Future[A] = {
    val p = Promise[A]()

    val listener =
      new Runnable {
        override def run(): Unit = {
          try p.success(lf.get())
          catch {
            case NonFatal(e) =>
              p.failure(e)
          }
        }
      }

    lf.addListener(listener, new Executor {
      override def execute(command: Runnable) = ec.execute(command)
    })

    p.future
  }

}

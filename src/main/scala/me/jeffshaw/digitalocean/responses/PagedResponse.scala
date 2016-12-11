package me.jeffshaw.digitalocean
package responses

import me.jeffshaw.digitalocean.ToFuture._
import org.asynchttpclient.RequestBuilder
import scala.concurrent._

private[digitalocean] case class PagedResponse[T, P <: responses.Page[T]] (
  client: DigitalOceanClient,
  current: P
)(implicit val ec: ExecutionContext,
  mf: Manifest[P]
) extends Iterable[T] {

  private def next: Option[Future[PagedResponse[T, P]]] = {
    for {
      links <- current.links
      pages <- links.pages
      nextPageUrl <- pages.next
    } yield {
      //Avoid a redirect.
      val secure =
        if (nextPageUrl.startsWith("http://")) {
          val builder = new StringBuilder(nextPageUrl)
          builder.insert(4, 's').toString
        } else nextPageUrl

      val nextPageRequest = new RequestBuilder("GET").setUrl(secure).build()
      for {
        response <- client.customRequest[P](nextPageRequest)
      } yield {
        PagedResponse[T, P](client, response)
      }
    }
  }

  def length: Option[BigInt] = {
    current.meta.map(_.total)
  }

  override def iterator: Iterator[T] = {
    def nextIterator = {
      next.map(f => Await.result(f, client.maxWaitPerRequest).iterator).
        getOrElse(Iterator.empty)
    }

    current.page.toIterator ++ nextIterator
  }
}

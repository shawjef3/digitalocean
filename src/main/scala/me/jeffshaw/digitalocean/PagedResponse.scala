package me.jeffshaw.digitalocean

import scala.concurrent._

case class PagedResponse[T, P <: responses.Page[T]] (
  client: DigitalOceanClient,
  implicit val ec: ExecutionContext,
  current: P
)(implicit mf: Manifest[P]) extends Iterable[T] {

  private def next: Option[Future[PagedResponse[T, P]]] = {
    for {
      links <- current.links
      pages <- links.pages
      nextPageUrl <- pages.next
    } yield {
        val nextPageRequest = dispatch.url(nextPageUrl).GET
        for {
          response <- client.customRequest[P](nextPageRequest)
        } yield {
          PagedResponse[T, P](client, ec, response)
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

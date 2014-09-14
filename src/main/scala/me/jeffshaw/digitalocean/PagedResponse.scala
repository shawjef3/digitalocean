package me.jeffshaw.digitalocean

import scala.concurrent._, duration._

case class PagedResponse[T, P <: responses.Page[T]] (
  client: DigitalOceanClient,
  implicit val ec: ExecutionContext,
  current: P,
  maxWaitPerPage: Duration
)(implicit mf: Manifest[P]) extends Iterable[T] {

  private def next: Option[Future[PagedResponse[T, P]]] = {
    for {
      links <- current.links
      nextPageUrl <- links.pages.next
    } yield {
        val nextPageRequest = dispatch.url(nextPageUrl).GET
        for {
          response <- client.customRequest[P](nextPageRequest)
        } yield {
          PagedResponse[T, P](client, ec, response, maxWaitPerPage)
        }
    }
  }

  def length: Option[BigInt] = {
    current.meta.map(_.total)
  }

  override def iterator: Iterator[T] = {
    def nextIterator = {
      next.map(f => Await.result(f, maxWaitPerPage).iterator).
        getOrElse(Iterator.empty)
    }

    current.page.toIterator ++ nextIterator
  }
}

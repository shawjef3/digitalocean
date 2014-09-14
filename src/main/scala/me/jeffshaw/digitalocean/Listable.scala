package me.jeffshaw.digitalocean

import scala.concurrent._, duration._

trait Listable[T, P <: responses.Page[T]] {
  self: Path =>

  def list(maxWaitPerPage: Duration)(implicit client: DigitalOceanClient, ec: ExecutionContext, mf: Manifest[P]): Future[Iterator[T]] = {
    val pagedResponse = for {
      response <- client.get[P](path: _*)
    } yield {
      PagedResponse[T, P](
        client,
        ec,
        response,
        maxWaitPerPage
      )
    }
    pagedResponse.map(_.iterator)
  }

  def list(implicit client: DigitalOceanClient, ec: ExecutionContext, mf: Manifest[P]): Future[Iterator[T]] = {
    list(client.maxWaitPerPage)
  }

  def size(implicit client: DigitalOceanClient, ec: ExecutionContext, mf: Manifest[P]): Future[BigInt] = {
    for {
      response <- client.customRequest[P](DigitalOceanClient.host.addQueryParameter("per-page", "0"))
    } yield {
      response.meta.get.total
    }
  }
}

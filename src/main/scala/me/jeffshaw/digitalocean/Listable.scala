package me.jeffshaw.digitalocean

import me.jeffshaw.digitalocean.responses.PagedResponse

import scala.concurrent._

trait Listable[T, P <: responses.Page[T]] {
  self: Path =>

  def list(implicit client: DigitalOceanClient, ec: ExecutionContext, mf: Manifest[P]): Future[Iterator[T]] = {
    val pagedResponse = for {
      response <- client.get[P](path: _*)
    } yield {
      PagedResponse[T, P](
        client,
        ec,
        response
      )
    }
    pagedResponse.map(_.iterator)
  }

  def size(implicit client: DigitalOceanClient, ec: ExecutionContext, mf: Manifest[P]): Future[BigInt] = {
    for {
      response <- client.customRequest[P](DigitalOceanClient.host.addQueryParameter("per-page", "0"))
    } yield {
      response.meta.get.total
    }
  }
}

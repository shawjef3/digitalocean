package me.jeffshaw.digitalocean

import me.jeffshaw.digitalocean.responses.PagedResponse
import org.json4s.JValue
import scala.concurrent._

trait Listable[T, P <: responses.Page[T]] {
  self: Path =>

  def list()(implicit client: DigitalOceanClient, ec: ExecutionContext, mf: Manifest[P]): Future[Iterator[T]] = {
    Listable.listGet[T, P](path, queryParameters)
  }

  def size()(implicit client: DigitalOceanClient, ec: ExecutionContext, mf: Manifest[P]): Future[BigInt] = {
    for {
      response <- client.get[P](path, queryParameters ++ Listable.listZeroQueryParameters)
    } yield {
      response.meta.get.total
    }
  }

}

object Listable {
  val listZeroQueryParameters = Map("per-page" -> Seq("0"))

  def listGet[
    T,
    P <: responses.Page[T]
  ](path: Seq[String],
    queryParameters: Map[String, Seq[String]] = Map.empty
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext,
    mf: Manifest[P]
  ): Future[Iterator[T]] = {
    val pagedResponse = for {
      response <- client.get[P](path, queryParameters)
    } yield {
      PagedResponse[T, P](
        client,
        ec,
        response
      )
    }
    pagedResponse.map(_.iterator)
  }

  def listPost[
    T,
    P <: responses.Page[T]
  ](path: Seq[String],
    message: JValue,
    queryParameters: Map[String, Seq[String]] = Map.empty
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext,
    mf: Manifest[P]
  ): Future[Iterator[T]] = {
    val pagedResponse = for {
      response <- client.post[P](path, message, queryParameters)
    } yield {
      PagedResponse[T, P](
        client,
        ec,
        response
      )
    }
    pagedResponse.map(_.iterator)
  }

}

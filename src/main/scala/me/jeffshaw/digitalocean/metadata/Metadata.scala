package me.jeffshaw.digitalocean.metadata

import dispatch.Http
import me.jeffshaw.digitalocean.RegionEnum
import org.json4s.Extraction
import org.json4s.native.JsonMethods
import scala.concurrent.{ExecutionContext, Future}

case class Metadata(
  dropletId: BigInt,
  hostname: String,
  vendorData: String,
  publicKeys: Seq[String],
  region: RegionEnum,
  interfaces: Interfaces,
  floatingIp: Option[FloatingIp],
  dns: Dns
)

object Metadata {
  import me.jeffshaw.digitalocean.formats

  private[digitalocean] def apply(metadata: String): Metadata = {
    val json = JsonMethods.parse(metadata)
    Extraction.extract[responses.Metadata](json).toMetadata
  }

  def apply()(implicit ec: ExecutionContext): Future[Metadata] = {
    for {
      response <- Http(endpoint GET)
    } yield Metadata(response.getResponseBody)
  }

  val endpoint = dispatch.host("169.254.169.254") / "metadata" / "v1.json"
}

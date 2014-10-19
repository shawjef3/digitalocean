package me.jeffshaw.digitalocean.metadata

import java.net.InetAddress

import dispatch.Http
import me.jeffshaw.digitalocean.RegionEnum
import org.json4s.Extraction
import org.json4s.native.JsonMethods

import scala.concurrent.{ExecutionContext, Future}

case class Metadata(
  id: BigInt,
  hostname: String,
  vendorData: String,
  publicKeys: Seq[String],
  region: RegionEnum,
  interfaces: Interfaces,
  nameservers: Seq[InetAddress]
)

object Metadata {
  import me.jeffshaw.digitalocean.formats

  def apply()(implicit ec: ExecutionContext): Future[Metadata] = {
    for {
      response <- Http(endpoint GET)
    } yield {
      val json = JsonMethods.parse(response.getResponseBody)
      val rawMetadata = Extraction.extract[responses.Metadata](json)
      rawMetadata.toMetadata
    }
  }

  val endpoint = dispatch.host("169.254.169.254") / "metadata" / "v1.json"
}

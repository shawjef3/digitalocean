package me.jeffshaw.digitalocean

import me.jeffshaw.digitalocean.responses.HasBiMapSerializer
import org.json4s._

sealed trait NetworkType

case object Public extends NetworkType

case object Private extends NetworkType

private[digitalocean] object NetworkType extends HasBiMapSerializer[NetworkType] {
  override private[digitalocean] val jsonMap: Map[NetworkType, JValue] =
    Map(
      Public -> JString("public"),
      Private -> JString("private")
    )
}

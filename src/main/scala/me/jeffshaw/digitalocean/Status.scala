package me.jeffshaw.digitalocean

import me.jeffshaw.digitalocean.responses.HasBiMapSerializer
import org.json4s.JValue
import org.json4s.JsonAST.JString

sealed trait Status

case object New extends Status

case object Active extends Status

case object Off extends Status

case object Archive extends Status

object Status extends HasBiMapSerializer[Status] {
  override private[digitalocean] val jsonMap: Map[Status, JValue] =
    Map[Status, JValue](
      New -> JString("new"),
      Active -> JString("active"),
      Off -> JString("off"),
      Archive -> JString("archive")
    )
}

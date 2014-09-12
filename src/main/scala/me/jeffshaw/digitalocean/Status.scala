package me.jeffshaw.digitalocean

import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

sealed trait Status

case object New extends Status

case object Active extends Status

case object Off extends Status

case object Archive extends Status

object Status {
  case object Serializer extends CustomSerializer[Status](format =>
    (
      {
        case JString("new") => New
        case JString("active") => Active
        case JString("off") => Off
        case JString("archive") => Archive
      },
      {
        case New => JString("new")
        case Active => JString("active")
        case Off => JString("off")
        case Archive => JString("archive")
      }
    )
  )
}

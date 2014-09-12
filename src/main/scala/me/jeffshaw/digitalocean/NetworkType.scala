package me.jeffshaw.digitalocean

import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

sealed trait NetworkType

case object Public extends NetworkType

case object Private extends NetworkType

object NetworkType {
  case object Serializer extends CustomSerializer[NetworkType](format =>
    (
      {
      case JString("public") =>
        Public
      case JString("private") =>
        Private
      },
      {
        case Public =>
          JString("public")
        case Private =>
          JString("private")
      }
    )
  )
}

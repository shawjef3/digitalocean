package me.jeffshaw.digitalocean

import org.json4s._

private[digitalocean] case object ActionStatusSerializer extends CustomSerializer[Action.Status](format =>
  (
    {
      case JString("in-progress") =>
        Action.InProgress
      case JString("completed") =>
        Action.Completed
      case JString("errored") =>
        Action.Errored
    },
    {
      case Action.InProgress =>
        JString("in-progress")
      case Action.Completed =>
        JString("completed")
      case Action.Errored =>
        JString("errored")
    }
  )
)

package me.jeffshaw.digitalocean

import java.time.Instant

import org.json4s._

private[digitalocean] case object InstantSerializer extends CustomSerializer[Instant](format =>
	(
		{
			case JString(s) =>
        //ISO8601
				Instant.parse(s)
		},
		{
			case i: Instant =>
        //ISO8601
				JString(i.toString)
		}
	)
)

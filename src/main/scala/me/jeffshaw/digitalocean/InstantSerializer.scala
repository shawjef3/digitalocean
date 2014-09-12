package me.jeffshaw.digitalocean

import java.time.Instant

import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

case object InstantSerializer extends CustomSerializer[Instant](format =>
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

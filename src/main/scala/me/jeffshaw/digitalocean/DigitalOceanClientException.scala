package me.jeffshaw.digitalocean

import org.asynchttpclient.Response

case class DigitalOceanClientException(
  response: Response,
  cause: Option[Throwable] = None
) extends Exception(s"unexpected response from ${response.getUri}: ${response.getResponseBody()}", cause.orNull)

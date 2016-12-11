package me.jeffshaw.digitalocean

import org.asynchttpclient.Response

case class DigitalOceanClientException(response: Response)
  extends Exception(response.getResponseBody)

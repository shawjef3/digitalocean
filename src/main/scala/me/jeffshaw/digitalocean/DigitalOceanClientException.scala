package me.jeffshaw.digitalocean

import com.ning.http.client.Response

case class DigitalOceanClientException(response: Response)
  extends Exception(response.getResponseBody)

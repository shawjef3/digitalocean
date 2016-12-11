package me.jeffshaw.digitalocean

import java.util.concurrent.TimeUnit
import com.typesafe.config.ConfigFactory
import org.scalatest._
import scala.concurrent._
import duration._
import org.asynchttpclient.DefaultAsyncHttpClient

abstract class Suite extends FunSuite with Matchers {
  implicit val httpClient = new DefaultAsyncHttpClient()

  val config = ConfigFactory.load()

  implicit val client = DigitalOceanClient(
    token = config.getString("digital_ocean_api_token"),
    maxWaitPerRequest = config.getDuration("max_wait_per_request", TimeUnit.MILLISECONDS) milliseconds,
    actionCheckInterval = config.getDuration("action_check_interval", TimeUnit.MILLISECONDS) milliseconds
  )

  val testImageSlug = config.getString("image_slug")

  val testRegionSlug = config.getString("region")

  implicit val ec = ExecutionContext.global

  val volumeNamePrefix = "scala-test-"

  val dropletNamePrefix = "ScalaTest"

}

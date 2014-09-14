package me.jeffshaw.digitalocean

import com.typesafe.config.ConfigFactory
import org.scalatest._

import scala.concurrent._, duration._

class Spec extends FunSuite with Matchers {
  val config = ConfigFactory.load()

  implicit val client = DigitalOceanClient(config.getString("digital_ocean_api_token"), 5 seconds)

  implicit val ec = ExecutionContext.global
}

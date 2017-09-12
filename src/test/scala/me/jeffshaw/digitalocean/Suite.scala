package me.jeffshaw.digitalocean

import java.util.concurrent.TimeUnit
import com.typesafe.config.ConfigFactory
import org.scalatest._
import scala.concurrent._
import duration._
import org.asynchttpclient.DefaultAsyncHttpClient

abstract class Suite
  extends FunSuite
  with Matchers
  with BeforeAndAfterAll {

  val config = ConfigFactory.load()

  implicit val httpClient = new DefaultAsyncHttpClient()

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

  val firewallNamePrefix = volumeNamePrefix

  def listDroplets(): Future[List[Droplet]] = {
    for {
      droplets <- Droplet.list()
    } yield droplets.toList.filter(_.name.startsWith(dropletNamePrefix))
  }

  def listDropletActions(): Future[List[Action]] =
    for {
      droplets <- listDroplets()
      actions <- Future.sequence(droplets.map(_.actions().map(_.toList)))
    } yield actions.flatten

  def deleteDroplets(): Future[List[DropletDeletion]] = {
    for {
      droplets <- Droplet.list()
      testDroplets = droplets.toList.filter(_.name.startsWith(dropletNamePrefix))
      _ <- client.poll[List[Action]](listDropletActions(), _.forall(_.status != Action.InProgress))
      deletes <- Future.sequence(testDroplets.map(_.delete))
    } yield deletes
  }

  def listVolumes(): Future[List[Volume]] = {
    for {
      volumes <- Volume.list()
    } yield volumes.toList.filter(_.name.startsWith(volumeNamePrefix))
  }

  def listFirewalls(): Future[List[Firewall]] = {
    for {
      firewalls <- Firewall.list()
    } yield firewalls.toList.filter(_.name.startsWith(firewallNamePrefix))
  }

  override protected def afterAll(): Unit = {
    httpClient.close()
  }

}

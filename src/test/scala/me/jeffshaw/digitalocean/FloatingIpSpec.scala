package me.jeffshaw.digitalocean

import scala.concurrent.Await

class FloatingIpSpec extends Suite {

  var createdIp: Option[FloatingIp] = None

  test("creates a floating ip") {
    createdIp = Some(Await.result(FloatingIp.create(RegionEnum.fromSlug(testRegionSlug)), client.maxWaitPerRequest))

    assert(createdIp.isDefined)
  }

  test("assigns a floating ip to a droplet") {
    val ip = createdIp.get

    val floatingIps = Await.result(FloatingIp.list(), client.maxWaitPerRequest)

    assert(floatingIps.contains(ip))
  }

  test("deletes a floating ip") {
    val ip = createdIp.get

    val deleted = for {
      () <- ip.delete()
      floatingIps <- FloatingIp.list()
    } yield {
      ! floatingIps.contains(ip)
    }

    assert(Await.result(deleted, client.maxWaitPerRequest * 2))
  }

}

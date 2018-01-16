package me.jeffshaw.digitalocean

import scala.concurrent.Await
import scala.concurrent.duration._

class FloatingIpSpec extends Suite {
  test("creates and deletes a floating ip assigned to a region") {
    val region = RegionEnum.fromSlug(testRegionSlug)
    for {
      ip <- FloatingIp.create(region)
      deleted <- ip.delete()
    } yield {
      assertResult(region)(ip.region.toEnum)
    }
  }

  test("creates and deletes a floating ip assigned to a droplet") {
    withDroplet { droplet =>
      for {
        ip <- FloatingIp.create(droplet.id)
        complete <- ip.complete()
        afterAssignment <- FloatingIp(ip.ip)
        unassignAction <- ip.unassign()
        completedUnassignAction <- unassignAction.complete()
        afterUnassignment <- FloatingIp(ip.ip)
        () <- ip.delete()
      } yield {
        assertResult(Action.Completed)(complete.status)
        assertResult(Some(droplet))(afterAssignment.droplet)
        assert(afterUnassignment.droplet.isEmpty)
      }
    }
  }

  override protected def afterAll(): Unit = {
    Await.result(deleteDroplets(), 3 minutes)
    super.afterAll()
  }
}

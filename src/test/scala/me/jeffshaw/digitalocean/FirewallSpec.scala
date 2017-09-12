package me.jeffshaw.digitalocean

import java.net.InetAddress
import scala.concurrent.{Await, Future}
import scala.util.Random
import scala.concurrent.duration._

class FirewallSpec extends Suite {

  test("create, update") {
    val name = firewallNamePrefix + Random.nextInt()
    val inboundRule = Firewall.InboundRule(
      protocol = Firewall.Protocol.Tcp(Firewall.Port.Range(1, 2)),
      sources = Firewall.Source(addresses = Seq(Firewall.Source.Address(InetAddress.getByName("1.1.1.1"), cidr = Some(8))))
    )
    val create = Firewall.create(name, inboundRules = Seq(inboundRule), outboundRules = Seq())
    val firewall = Await.result(create, 20 seconds)

    assertResult(name)(firewall.name)
    assertResult(Seq(inboundRule))(firewall.inboundRules)
    assert(firewall.outboundRules.isEmpty)
    assert(firewall.tags.isEmpty)
    assert(firewall.dropletIds.isEmpty)


    val outboundRule =
      Firewall.OutboundRule(
        protocol = Firewall.Protocol.Icmp,
        destinations = Firewall.Destination(
          tags = Seq("hi")
        )
      )

    val updated =
      Await.result(
        firewall.update(
          inboundRules = Seq(),
          outboundRules = Seq(outboundRule)
        ),
        20 seconds
      )

    assert(updated.inboundRules.isEmpty)
    assertResult(Seq(outboundRule))(updated.outboundRules)
  }

  override protected def afterAll(): Unit = {
    val deletes =
      for {
        firewalls <- listFirewalls()
        deletes <- Future.sequence(firewalls.map(_.delete()))
      } yield deletes

    Await.result(deletes, 1 minute)
  }
}

object FirewallSpec {

  import Firewall._


}
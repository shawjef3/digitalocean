package me.jeffshaw.digitalocean

import java.net.InetAddress
import scala.concurrent.{Await, Future}
import scala.util.Random
import scala.concurrent.duration._

class FirewallSpec extends Suite {

  val randomSuffix = Random.nextInt().toString

  test("create, update") {
    val name = firewallNamePrefix + randomSuffix
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
          tags = Seq(name)
        )
      )

    val tag = Await.result(Tag.create(name), 10 seconds)

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
    Await.result(tag.delete(), 10 seconds)
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
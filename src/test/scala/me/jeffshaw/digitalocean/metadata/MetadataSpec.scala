package me.jeffshaw.digitalocean.metadata

import java.net._
import me.jeffshaw.digitalocean._

class MetadataSpec extends Suite {

  test("metadata example is correctly parsed") {
    /**
      * https://developers.digitalocean.com/metadata/#metadata-in-json
      */
    val metadata = Metadata(io.Source.fromInputStream(getClass.getResourceAsStream("example.json")).getLines.mkString)

    assertResult(metadata.dropletId)(BigInt(1))
    assertResult(metadata.hostname)("www.jeffshaw.me")
    assertResult(metadata.region)(NewYork3)
    assertResult(metadata.interfaces.`private`.size)(1)
    assertResult(metadata.interfaces.public.size)(1)

    val `private` = metadata.interfaces.`private`.head

    assertResult(Some(Ipv4(InetAddress.getByName("10.132.1.179").asInstanceOf[Inet4Address], "255.255.0.0", InetAddress.getByName("10.132.0.1").asInstanceOf[Inet4Address])))(`private`.ipv4)
    assertResult("04:01:2b:8b:74:02")(`private`.mac)
    assertResult(Private)(`private`.`type`)

    val public = metadata.interfaces.public.head

    assertResult(Some(Ipv6(InetAddress.getByName("2604:A880:0800:0010:0000:0000:00BB:A001").asInstanceOf[Inet6Address], 64, InetAddress.getByName("2604:A880:0800:0010:0000:0000:0000:0001").asInstanceOf[Inet6Address])))(public.ipv6)
    assertResult(Public)(public.`type`)

    assertResult(Some(Ipv4(InetAddress.getByName("10.17.0.6").asInstanceOf[Inet4Address], "255.255.0.0", InetAddress.getByName("10.17.0.1").asInstanceOf[Inet4Address])))(public.anchorIpv4)

    val nameservers = Seq(
      "8.8.8.8",
      "8.8.4.4"
    ).map(InetAddress.getByName)

    assertResult(nameservers)(metadata.dns.nameservers)
  }
}

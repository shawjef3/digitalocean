package me.jeffshaw.digitalocean.metadata

import java.net._

import me.jeffshaw.digitalocean._
import org.json4s.Extraction
import org.json4s.native.JsonMethods

class MetadataSpec extends Spec {

  test("metadata example is correctly parsed") {
    implicit val formats = me.jeffshaw.digitalocean.formats
    val json = JsonMethods.parse(MetadataSpec.example)
    val rawMetadata = Extraction.extract[responses.Metadata](json)
    val metadata = rawMetadata.toMetadata

    assert(metadata.id == BigInt(2756294))
    assert(metadata.hostname == "sample-droplet")
    assert(metadata.region == NewYork3)
    assert(metadata.interfaces.`private`.size == 1)
    assert(metadata.interfaces.public.size == 1)
    assert(metadata.nameservers.size == 3)

    val `private` = metadata.interfaces.`private`.head

    assert(`private`.ipv4.get == Ipv4(InetAddress.getByName("10.132.255.113").asInstanceOf[Inet4Address], "255.255.0.0", InetAddress.getByName("10.132.0.1").asInstanceOf[Inet4Address]))
    assert(`private`.`type` == Private)

    val public = metadata.interfaces.public.head

    assert(public.ipv6.get == Ipv6(InetAddress.getByName("2604:A880:0800:0010:0000:0000:017D:2001").asInstanceOf[Inet6Address], 64, InetAddress.getByName("2604:A880:0800:0010:0000:0000:0000:0001").asInstanceOf[Inet6Address]))
    assert(public.`type` == Public)

    val nameservers = Seq("2001:4860:4860::8844",
      "2001:4860:4860::8888",
      "8.8.8.8"
    ).map(InetAddress.getByName)

    assert(metadata.nameservers == nameservers)
  }
}

object MetadataSpec {
  /**
   * https://developers.digitalocean.com/metadata/#metadata-in-json
   */
  val example =
    """{
      |  "droplet_id":2756294,
      |  "hostname":"sample-droplet",
      |  "vendor_data":"#cloud-config\ndisable_root: false\nmanage_etc_hosts: true\n\ncloud_config_modules:\n - ssh\n - set_hostname\n - [ update_etc_hosts, once-per-instance ]\n\ncloud_final_modules:\n - scripts-vendor\n - scripts-per-once\n - scripts-per-boot\n - scripts-per-instance\n - scripts-user\n",
      |  "public_keys":["ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCcbi6cygCUmuNlB0KqzBpHXf7CFYb3VE4pDOf/RLJ8OFDjOM+fjF83a24QktSVIpQnHYpJJT2pQMBxD+ZmnhTbKv+OjwHSHwAfkBullAojgZKzz+oN35P4Ea4J78AvMrHw0zp5MknS+WKEDCA2c6iDRCq6/hZ13Mn64f6c372JK99X29lj/B4VQpKCQyG8PUSTFkb5DXTETGbzuiVft+vM6SF+0XZH9J6dQ7b4yD3sOder+M0Q7I7CJD4VpdVD/JFa2ycOS4A4dZhjKXzabLQXdkWHvYGgNPGA5lI73TcLUAueUYqdq3RrDRfaQ5Z0PEw0mDllCzhk5dQpkmmqNi0F sammy@digitalocean.com"],
      |  "region":"nyc3",
      |  "interfaces":{
      |    "private":[
      |      {
      |        "ipv4":{
      |          "ip_address":"10.132.255.113",
      |          "netmask":"255.255.0.0",
      |          "gateway":"10.132.0.1"
      |        },
      |        "mac":"04:01:2a:0f:2a:02",
      |        "type":"private"
      |      }
      |    ],
      |    "public":[
      |      {
      |        "ipv4":{
      |          "ip_address":"104.131.20.105",
      |          "netmask":"255.255.192.0",
      |          "gateway":"104.131.0.1"
      |        },
      |        "ipv6":{
      |          "ip_address":"2604:A880:0800:0010:0000:0000:017D:2001",
      |          "cidr":64,
      |          "gateway":"2604:A880:0800:0010:0000:0000:0000:0001"
      |        },
      |        "mac":"04:01:2a:0f:2a:01",
      |        "type":"public"}
      |    ]
      |  },
      |  "dns":{
      |    "nameservers":[
      |      "2001:4860:4860::8844",
      |      "2001:4860:4860::8888",
      |      "8.8.8.8"
      |    ]
      |  }
      |}""".stripMargin
}

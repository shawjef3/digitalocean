package me.jeffshaw.digitalocean

import me.jeffshaw.digitalocean.dns.Domain
import scala.util.Random

class DomainSpec extends Suite {

  val domainName = "scalatest" + Random.nextInt() + ".com"

  test("Domains can be created, listed, and deleted") {
    for {
      domain <- Domain.create(domainName, "10.0.0.1")
      domains <- Domain.list()
      _ = assert(domains.exists(_.name == domainName))
      () <- domain.delete()
    } yield succeed
  }

  test("Domains can have records added, listed and deleted") {
    for {
      domain <- Domain.create(domainName, "10.0.0.1")
      aRecord <- domain.createA("host." + domainName + ".", "10.0.0.1")
      allRecords <- domain.records()
      _ = assert(allRecords.contains(aRecord))
      () <- aRecord.delete()
      allWithoutARecord <- domain.records()
      _ = assert(! allRecords.contains(aRecord))
      () <- domain.delete()
    } yield succeed
  }

  override protected def afterAll(): Unit = {
    scala.util.Try(Domain.delete(domainName))
    super.afterAll()
  }
}

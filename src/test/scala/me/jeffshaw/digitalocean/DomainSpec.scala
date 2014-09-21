package me.jeffshaw.digitalocean

import concurrent._, duration._
import scala.util.Random

class DomainSpec extends Spec {

  test("Domains can be created, listed, and deleted") {

    val domainName = "test" + Random.nextInt() + ".com"

    val domain = Await.result(Domain.create(domainName, "10.0.0.1"), 30 seconds)

    val domains = Await.result(Domain.list, 10 seconds)

    assert(domains.size > 0)

    Await.result(domain.delete, 10 seconds)
  }

  test("Domains can have records added, listed and deleted") {
    val domainName = "test" + Random.nextInt() + ".com"

    val domain = Await.result(Domain.create(domainName, "10.0.0.1"), 30 seconds)

    val aRecord = Await.result(domain.createA("host." + domainName + ".", "10.0.0.1"), 10 seconds)

    var allRecords = Await.result(domain.records, 10 seconds)

    assert(allRecords.contains(aRecord))

    Await.result(aRecord.delete, 10 seconds)

    allRecords = Await.result(domain.records, 10 seconds)

    assert(! allRecords.contains(aRecord))

    Await.result(domain.delete, 10 seconds)
  }

}

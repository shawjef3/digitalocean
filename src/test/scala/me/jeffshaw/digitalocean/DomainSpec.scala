package me.jeffshaw.digitalocean

import me.jeffshaw.digitalocean.dns.Domain
import org.scalatest.BeforeAndAfterAll

import concurrent._, duration._
import scala.util.Random

class DomainSpec extends Spec with BeforeAndAfterAll {

  val domainName = "test" + Random.nextInt() + ".com"

  test("Domains can be created, listed, and deleted") {

    val domain = Await.result(Domain.create(domainName, "10.0.0.1"), 30 seconds)

    val domains = Await.result(Domain.list, 10 seconds)
    
    assert(domains.exists(_.name == domainName))

    Await.result(domain.delete, 10 seconds)
  }

  test("Domains can have records added, listed and deleted") {

    val domain = Await.result(Domain.create(domainName, "10.0.0.1"), 30 seconds)

    val aRecord = Await.result(domain.createA("host." + domainName + ".", "10.0.0.1"), 10 seconds)

    var allRecords = Await.result(domain.records, 10 seconds)

    assert(allRecords.contains(aRecord))

    Await.result(aRecord.delete, 10 seconds)

    allRecords = Await.result(domain.records, 10 seconds)

    assert(! allRecords.contains(aRecord))

    Await.result(domain.delete, 10 seconds)
  }

  override protected def afterAll(): Unit = {
    scala.util.Try(Domain.delete(domainName))
  }
}

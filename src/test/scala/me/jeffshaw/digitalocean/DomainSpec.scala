package me.jeffshaw.digitalocean

import me.jeffshaw.digitalocean.dns.Domain
import org.scalatest.BeforeAndAfterAll

import concurrent._, duration._
import scala.util.Random

class DomainSpec extends Spec with BeforeAndAfterAll {

  val domainName = "scalatest" + Random.nextInt() + ".com"

  test("Domains can be created, listed, and deleted") {

    val t = for {
      domain <- Domain.create(domainName, "10.0.0.1")
      domains <- Domain.list
      () = assert(domains.exists(_.name == domainName))
      () <- domain.delete
    } yield ()

    Await.result(t, 10 seconds)
  }

  test("Domains can have records added, listed and deleted") {

    val t = for {
      domain <- Domain.create(domainName, "10.0.0.1")
      aRecord <- domain.createA("host." + domainName + ".", "10.0.0.1")
      allRecords <- domain.records
      () = assert(allRecords.contains(aRecord))
      () <- aRecord.delete
      allWithoutARecord <- domain.records
      () = assert(! allRecords.contains(aRecord))
      () <- domain.delete
    } yield ()

    Await.result(t, 30 seconds)
  }

  override protected def afterAll(): Unit = {
    scala.util.Try(Domain.delete(domainName))
  }
}

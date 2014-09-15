package me.jeffshaw

import java.time.Instant

import org.json4s._

import scala.annotation.tailrec
import scala.concurrent._, duration._

package object digitalocean {
  implicit val formats = {
    DefaultFormats.withBigDecimal +
      InstantSerializer +
      NetworkType.Serializer +
      Status.Serializer +
      Inet4AddressSerializer +
      Inet6AddressSerializer +
      ActionResourceTypeSerializer +
      ActionStatusSerializer
  }

  implicit def Region2RegionEnum(r: Region): RegionEnum = {
    r.toEnum
  }

  implicit def String2RegionEnum(slug: String): RegionEnum = {
    RegionEnum.fromSlug(slug)
  }

  implicit def Size2SizeEnum(size: Size): SizeEnum = {
    size.toEnum
  }

  implicit def String2SizeEnum(slug: String): SizeEnum = {
    SizeEnum.fromSlug(slug)
  }

  implicit class AwaitActions(actions: Iterable[Action]) {
    def await(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterable[Action]] = {
      Future.sequence(actions.map(_.await))
    }
  }

  implicit class AwaitActionFutures(actions: Iterable[Future[Action]]) {
    def await(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterable[Action]] = {
      for {
        actions <- Future.sequence(actions)
        completedActions <- actions.await
      } yield {
        completedActions
      }
    }
  }

  implicit def Droplet2Creation(dropletCreation: DropletCreation): Droplet = {
    dropletCreation.droplet
  }
}

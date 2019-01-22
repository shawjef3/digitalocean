package me.jeffshaw.digitalocean

import java.time.Instant
import me.jeffshaw.digitalocean.responses.{HasBiMapSerializer, PagedResponse}
import org.json4s.JValue
import org.json4s.JsonAST.JString
import scala.concurrent.{ExecutionContext, Future}

case class Image(
  id: BigInt,
  name: String,
  `type`: Image.Type,
  distribution: String,
  slug: Option[String],
  public: Boolean,
  regions: Seq[String],
  createdAt: Instant,
  minDiskSize: BigInt,
  sizeGigabytes: BigDecimal,
  description: Option[String],
  tags: Set[String],
  status: Image.Status,
  errorMessage: String
) {
  def delete(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    Image.delete(id)
  }

  override def hashCode(): Int = {
    id.hashCode()
  }

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case that: Image =>
        eq(that) || that.id == this.id
      case _ =>
        false
    }
  }
}

object Image
  extends Path {

  override val path: Seq[String] = Seq("images")

  def apply(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Image] = {
    apply(id.toString)
  }

  def apply(slug: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Image] = {
    val path = this.path :+ slug
    for {
      response <- client.get[responses.Image](path)
    } yield {
      response.image
    }
  }

  def delete(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    val path = this.path :+ id.toString
    client.delete(path)
  }

  def list(
    listType: ListType = ListType.All,
    `private`: Boolean = true
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext,
    mf: Manifest[responses.Images]
  ): Future[Iterator[Image]] = {
    val queryParameters =
      listType.queryParameters ++
        (if (`private`) Private.queryParameters else Map.empty)

    val pagedResponse = for {
      response <- client.get[responses.Images](path, queryParameters)
    } yield {
      PagedResponse[Image, responses.Images](
        client,
        response
      )
    }
    pagedResponse.map(_.iterator)
  }

  def size(
    listType: ListType = ListType.All,
    `private`: Boolean = true
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext,
    mf: Manifest[responses.Images]
  ): Future[BigInt] = {
    for {
      response <- client.get[responses.Images](path, queryParameters ++ Listable.listZeroQueryParameters)
    } yield {
      response.meta.get.total
    }
  }

  sealed trait Type

  object Type extends HasBiMapSerializer[Type] {

    case object Snapshot extends Type

    case object Backup extends Type

    case object Custom extends Type

    override private[digitalocean] val jsonMap: Map[Type, JValue] = Map(
      Snapshot -> JString("snapshot"),
      Backup -> JString("backup"),
      Custom -> JString("custom")
    )
  }

  sealed trait Status

  object Status extends HasBiMapSerializer[Status] {
    case object New extends Status
    case object Available extends Status
    case object Pending extends Status
    case object Deleted extends Status
    override private[digitalocean] val jsonMap: Map[Status, JValue] =
      Map(
        New -> JString("NEW"),
        Available -> JString("available"),
        Pending -> JString("pending"),
        Deleted -> JString("deleted")
      )
  }

  sealed trait ListType {
    val queryParameters: Map[String, Seq[String]]
  }

  object ListType {
    case object All extends ListType {
      override val queryParameters: Map[String, Seq[String]] = Map.empty
    }

    /**
      * Create from images that have pre-installed and configured programs that will get your Droplet off to a strong start.
      * See <a href="https://www.digitalocean.com/community/tutorials/how-to-create-your-first-digitalocean-droplet-virtual-server">https://www.digitalocean.com/community/tutorials/how-to-create-your-first-digitalocean-droplet-virtual-server</a>.
      */
    case object Application extends ListType {
      override val queryParameters: Map[String, Seq[String]] = Map("type" -> Seq("application"))
    }

    /**
      * Create from several operating systems, such as Ubuntu, Debian, CoreOS, and CentOS.
      * See <a href="https://www.digitalocean.com/community/tutorials/how-to-create-your-first-digitalocean-droplet-virtual-server">https://www.digitalocean.com/community/tutorials/how-to-create-your-first-digitalocean-droplet-virtual-server</a>.
      */
    case object Distribution extends ListType {
      override val queryParameters: Map[String, Seq[String]] = Map("type" -> Seq("distribution"))
    }
  }

  object Private {
    val queryParameters: Map[String, Seq[String]] = Map("private" -> Seq("true"))
  }

}

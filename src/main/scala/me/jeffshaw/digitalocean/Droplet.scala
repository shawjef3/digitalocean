package me.jeffshaw.digitalocean

import java.time.Instant
import org.json4s.JsonDSL._
import org.json4s._
import scala.concurrent._

case class Droplet(
  id: BigInt,
  name: String,
  memory: BigInt,
  vcpus: BigInt,
  disk: BigInt,
  region: Region,
  image: Image,
  kernel: Kernel,
  size: Size,
  locked: Boolean,
  createdAt: Instant,
  status: Status,
  networks: Networks,
  backupIds: Seq[BigInt],
  snapshotIds: Seq[BigInt],
  features: Seq[String],
  nextBackupWindow: BackupWindow,
  tags: Seq[String],
  volumeIds: Seq[String]
) {

  def exists(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Boolean] = {
    Droplet.exists(id)
  }

  def kernels(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Kernel]] = {
    Droplet.kernels(id)
  }

  def backups(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Image]] = {
    Droplet.backups(id)
  }

  def action(actionId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.action(id, actionId)
  }

  def actions()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Action]] = {
    Droplet.actions(id)
  }

  def delete()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[DropletDeletion] = {
    Droplet.delete(id)
  }

  def isDeleted()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Boolean] = {
    Droplet.isDeleted(id)
  }

  def reboot()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.reboot(id)
  }

  def snapshots()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Image]] = {
    Droplet.snapshots(id)
  }

  def powerCycle()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.powerCycle(id)
  }

  def shutdown()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.shutdown(id)
  }

  def powerOff()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.powerOff(id)
  }

  def powerOn()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.powerOn(id)
  }

  def passwordReset()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.passwordReset(id)
  }

  def resize(size: SizeEnum)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.resize(id, size)
  }

  def restore(image: Image)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.restore(id, image)
  }

  def restore(imageId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.restore(id, imageId)
  }

  def rebuild(imageSlug: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.rebuild(id, imageSlug)
  }

  def rebuild(imageId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.rebuild(id, imageId)
  }

  def rebuild(image: Image)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.rebuild(id, image)
  }

  def rename(name: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.rename(id, name)
  }

  def changeKernel(kernelId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.changeKernel(id, kernelId)
  }

  def changeKernel(kernel: Kernel)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.changeKernel(id, kernel)
  }

  def enableIPv6()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.enableIPv6(id)
  }

  def disableBackups(kernel: Kernel)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.disableBackups(id)
  }

  def enablePrivateNetworking(kernel: Kernel)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.enablePrivateNetworking(id)
  }

  def snapshot(name: Option[String] = None)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.snapshot(id, name)
  }

  def neighbors()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Droplet]] = {
    Droplet.neighbors(id)
  }

  def attach(volumeId: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Volume.attach(volumeId, id, region)
  }

  def detach(volumeId: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Volume.detach(volumeId, id, region)
  }

  def attach(volume: Volume)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    volume.attach(id)
  }

  def detach(volume: Volume)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    volume.detach(id)
  }

  def attachByName(name: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Volume.attachByName(name, id, region)
  }

  def detachByName(name: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Volume.detachByName(name, id, region)
  }

  def refresh()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Droplet] = {
   Droplet(id)
  }

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case that: Droplet =>
        eq(that) || this.id == that.id
      case _ =>
        false
    }
  }

  override def hashCode(): Int = id.hashCode()

}

object Droplet
  extends Path
  with Listable[Droplet, responses.Droplets] {

  override val path: Seq[String] = Seq("droplets")

  def apply(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Droplet] = {
    val path = this.path :+ id.toString
    for {
      response <- client.get[responses.Droplet](path)
    } yield {
      response.droplet
    }
  }

  def exists(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Boolean] = {
    val path = this.path :+ id.toString
    client.exists(path)
  }

  def isDeleted(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Boolean] = {
    for {
      exists <- exists(id)
    } yield {! exists}
  }

  def delete(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[DropletDeletion] = {
    val path = this.path :+ id.toString
    for {
      () <- client.delete(path)
    } yield {
      DropletDeletion(id)
    }
  }

  def delete(tag: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    client.delete(path, Map("tag_name" -> Seq(tag)))
  }

  def kernels(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Kernel]] = {
    Listable.listGet[Kernel, responses.Kernels](path ++ Seq(id.toString, "kernels"))
  }

  def snapshots(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Image]] = {
    Listable.listGet[Image, responses.Snapshots](this.path ++ Seq(id.toString, "snapshots"))
  }

  def backups(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Image]] = {
    Listable.listGet[Image, responses.Backups](this.path ++ Seq(id.toString, "backups"))
  }

  def action(id: BigInt, actionId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    val path = this.path ++ Seq(id.toString, "actions", actionId.toString)
    for {
      response <- client.get[responses.Action](path)
    } yield {
      response.action
    }
  }

  def actions(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Action]] = {
    Listable.listGet[Action, responses.Actions](this.path ++ Seq(id.toString, "actions"), Map.empty)
  }

  private def performAction(id: BigInt, action: String, argument: JObject = JObject(Nil))(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    val path = this.path ++ Seq(id.toString, "actions")
    client.post[responses.Action](path, ("type" -> action) ~ argument).map(_.action)
  }

  private def performTaggedActions(tag: String, action: String, argument: JObject = JObject(Nil))(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Action]] = {
    Listable.listPost[Action, responses.Actions](this.path :+ "actions", ("type" -> action) ~ argument, Map("tag_name" -> Seq(tag)))
  }

  def reboot(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "reboot")
  }

  def reboot(tag: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Action]] = {
    performTaggedActions(tag, "reboot")
  }

  def powerCycle(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "power_cycle")
  }

  def powerCycle(tag: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Action]] = {
    performTaggedActions(tag, "power_cycle")
  }

  def shutdown(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "shutdown")
  }

  def shutdown(tag: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Action]] = {
    performTaggedActions(tag, "shutdown")
  }

  def powerOff(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "power_off")
  }

  def powerOff(tag: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Action]] = {
    performTaggedActions(tag, "power_off")
  }

  def powerOn(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "power_on")
  }

  def powerOn(tag: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Action]] = {
    performTaggedActions(tag, "power_on")
  }

  def passwordReset(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "password_reset")
  }

  def resize(id: BigInt, size: SizeEnum)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "resize", ("size" -> size.slug))
  }

  def restore(id: BigInt, image: Image)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    restore(id, image.id)
  }

  def restore(id: BigInt, imageId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "restore", ("image" -> imageId))
  }

  def rebuild(id: BigInt, image: Image)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    rebuild(id, image.id)
  }

  def rebuild(id: BigInt, imageId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "rebuild", ("image" -> imageId))
  }

  def rebuild(id: BigInt, imageSlug: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "rebuid", ("image" -> imageSlug))
  }

  def rename(id: BigInt, name: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "rename", ("name" -> name))
  }

  def changeKernel(id: BigInt, kernel: Kernel)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    changeKernel(id, kernel.id)
  }

  def changeKernel(id: BigInt, kernelId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "change_kernel", ("kernel" -> kernelId))
  }

  def enableIPv6(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "enable_ipv6")
  }

  def enableIPv6(tag: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Action]] = {
    performTaggedActions(tag, "enable_ipv6")
  }

  def disableBackups(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "disable_backups")
  }

  def disableBackups(tag: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Action]] = {
    performTaggedActions(tag, "disable_backups")
  }

  def enablePrivateNetworking(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "enable_private_networking")
  }

  def enablePrivateNetworking(tag: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Action]] = {
    performTaggedActions(tag, "enable_private_networking")
  }

  def snapshot(id: BigInt, name: Option[String])(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "snapshot", ("name" -> name))
  }

  def snapshot(tag: String, name: Option[String])(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Action]] = {
    performTaggedActions(tag, "snapshot", ("name" -> name))
  }

  def neighbors(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Iterator[Droplet]] = {
    Listable.listGet[Droplet, responses.Droplets](path ++ Seq(id.toString, "neighbors"))
  }

  def create(
    name: String,
    region: RegionEnum,
    size: SizeEnum,
    image: Image,
    sshKeys: Seq[SshKey],
    backups: Boolean,
    ipv6: Boolean,
    privateNetworking: Boolean,
    userData: Option[String]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DropletCreation] = {
    create(name, region, size, image.id, sshKeys, backups, ipv6, privateNetworking, userData)
  }

  def create(
    name: String,
    region: RegionEnum,
    size: SizeEnum,
    image: BigInt,
    sshKeys: Seq[SshKey],
    backups: Boolean,
    ipv6: Boolean,
    privateNetworking: Boolean,
    userData: Option[String]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DropletCreation] = {
    val imagePart = "image" -> image
    createAux(name, region, size, imagePart, sshKeys, backups, ipv6, privateNetworking, userData)
  }

  def create(
    name: String,
    region: RegionEnum,
    size: SizeEnum,
    image: String,
    sshKeys: Seq[SshKey],
    backups: Boolean,
    ipv6: Boolean,
    privateNetworking: Boolean,
    userData: Option[String]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DropletCreation] = {
    val imagePart = "image" -> image
    createAux(name, region, size, imagePart, sshKeys, backups, ipv6, privateNetworking, userData)
  }

  private def createAux(
    name: String,
    region: RegionEnum,
    size: SizeEnum,
    imagePart: JValue,
    sshKeys: Seq[SshKey],
    backups: Boolean,
    ipv6: Boolean,
    privateNetworking: Boolean,
    userData: Option[String]
  )(implicit client: DigitalOceanClient,
    ec: ExecutionContext
  ): Future[DropletCreation] = {
    val nonImageParts =
      ("name" -> name) ~
        ("region" -> region.slug) ~
        ("size" -> size.slug) ~
        ("ssh_keys" -> sshKeys.map(_.id)) ~
        ("backups" -> backups) ~
        ("ipv6" -> ipv6) ~
        ("private_networking" -> privateNetworking) ~
        ("user_data" -> userData)

    val requestJson = nonImageParts.merge(imagePart)

    for {
      response <- client.post[responses.DropletCreation](path, requestJson)
      droplet <- response.toDropletCreation
    } yield {
      droplet
    }
  }

}

package me.jeffshaw.digitalocean

import java.time.Instant

import org.json4s.JsonDSL._
import org.json4s._

import scala.concurrent.{ExecutionContext, Future}

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
  features: Seq[String]
) {
  def kernels(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Kernel]] = {
    Droplet.kernels(id)
  }

  def backups(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Image]] = {
    Droplet.backups(id)
  }

  def action(actionId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.action(id, actionId)
  }

  def actions(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Action]] = {
    Droplet.actions(id)
  }

  def delete(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    Droplet.delete(id)
  }

  def reboot(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.reboot(id)
  }

  def snapshots(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Snapshot]] = {
    Droplet.snapshots(id)
  }

  def powerCycle(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.powerCycle(id)
  }

  def shutdown(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.shutdown(id)
  }

  def powerOff(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.powerOff(id)
  }

  def powerOn(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.powerOn(id)
  }

  def passwordReset(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.passwordReset(id)
  }

  def resize(size: Size)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.resize(id, size)
  }

  def resize(sizeSlug: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.resize(id, sizeSlug)
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

  def enableIPv6(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.enableIPv6(id)
  }

  def disableBackups(kernel: Kernel)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.disableBackups(id)
  }

  def enablePrivateNetworking(kernel: Kernel)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.enablePrivateNetworking(id)
  }

  def snapshot(name: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    Droplet.snapshot(id, name)
  }
}

object Droplet {
  def apply(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Droplet] = {
    for {
      response <- client.get[responses.Droplet]("droplets", id.toString)
    } yield {
      response.droplet
    }
  }

  def list(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Droplet]] = {
    for {
      response <- client.get[responses.Droplets]("droplets")
    } yield {
      response.droplets
    }
  }

  def delete(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    client.delete("droplets", id.toString)
  }

  def kernels(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Kernel]] = {
    for {
      response <- client.get[responses.Kernels]("droplet", id.toString, "kernels")
    } yield {
      response.kernels
    }
  }

  def snapshots(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Snapshot]] = {
    for {
      response <- client.get[responses.Snapshots]("droplets", id.toString, "snapshots")
    } yield {
      response.snapshots
    }
  }

  def backups(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Image]] = {
    for {
      response <- client.get[responses.Backups]("droplets", id.toString, "backups")
    } yield {
      response.backups
    }
  }

  def action(id: BigInt, actionId: BigInt)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    for {
      response <- client.get[responses.Action]("droplets", id.toString, "actions", actionId.toString)
    } yield {
      response.action
    }
  }

  def actions(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[Action]] = {
    for {
      response <- client.get[responses.Actions]("droplets", id.toString, "actions")
    } yield {
      response.actions
    }
  }

  private def performAction(id: BigInt, action: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    client.post[Action](("type" -> action), "droplets", id.toString, "actions")
  }

  def reboot(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "reboot")
  }

  def powerCycle(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "power_cycle")
  }

  def shutdown(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "shutdown")
  }

  def powerOff(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "power_off")
  }

  def powerOn(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "power_on")
  }

  def passwordReset(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "password_reset")
  }

  def resize(id: BigInt, size: Size)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    resize(id, size.slug)
  }

  def resize(id: BigInt, sizeSlug: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    client.post[Action](("type" -> "resize") ~ ("size" -> sizeSlug), "droplets", id.toString, "actions")
  }

  def restore(id: BigInt, image: Image)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    restore(id, image.id)
  }

  def restore(id: BigInt, imageId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    client.post[Action](("type" -> "restore") ~ ("image" -> imageId), "droplets", id.toString, "actions")
  }

  def rebuild(id: BigInt, image: Image)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    rebuild(id, image.id)
  }

  def rebuild(id: BigInt, imageId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    client.post[Action](("type" -> "rebuild") ~ ("image" -> imageId), "droplets", id.toString, "actions")
  }

  def rebuild(id: BigInt, imageSlug: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    client.post[Action](("type" -> "rebuild") ~ ("image" -> imageSlug), "droplets", id.toString, "actions")
  }

  def rename(id: BigInt, name: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    client.post[Action](("type" -> "rename") ~ ("name" -> name), "droplets", id.toString, "actions")
  }

  def changeKernel(id: BigInt, kernel: Kernel)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    changeKernel(id, kernel.id)
  }

  def changeKernel(id: BigInt, kernelId: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    client.post[Action](("type" -> "change_kernel") ~ ("kernel" -> kernelId), "droplets", id.toString, "actions")
  }

  def enableIPv6(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "enable_ipv6")
  }

  def disableBackups(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "disable_backups")
  }

  def enablePrivateNetworking(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    performAction(id, "enable_private_networking")
  }

  def snapshot(id: BigInt, name: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action] = {
    client.post[Action](("type" -> "snapshot") ~ ("name" -> name), "droplets", id.toString, "actions")
  }

  def create(
    name: String,
    region: RegionEnum,
    size: Size,
    image: Image,
    sshKeys: Seq[SshKey],
    backups: Boolean,
    ipv6: Boolean,
    privateNetworking: Boolean,
    userData: Option[String]
  )(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Droplet] = {
    create(name, region.slug, size.slug, image.id, sshKeys, backups, ipv6, privateNetworking, userData)
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
  )(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Droplet] = {
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
  )(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Droplet] = {
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
  )(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Droplet] = {
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
      response <- client.post[responses.Droplet](requestJson, "droplets")
    } yield {
      response.droplet
    }
  }
}

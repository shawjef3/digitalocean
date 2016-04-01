package me.jeffshaw.digitalocean

import org.json4s.JsonDSL._
import scala.concurrent.{ExecutionContext, Future}

/**
  *
  * @param id is Digital Ocean's identifier for the key
  * @param name is a friendly name for the key. It is ignored in equality checks.
  * @param fingerprint
  * @param publicKey
  */
case class SshKey(
  id: BigInt,
  name: String,
  fingerprint: String,
  publicKey: String
) {
  def setName(name: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[SshKey.Action.Create] = {
    SshKey.setNameById(id, name)
  }

  def delete()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[SshKey.Action.DeleteById] = {
    SshKey.deleteById(id)
  }

  override def hashCode(): Int = (id, fingerprint, publicKey).hashCode()

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case other: SshKey =>
        id == other.id &&
          fingerprint == other.fingerprint &&
          other.publicKey == other.publicKey
      case other: SshKey.Action.Create =>
        equals(other.sshKey)
    }
  }
}

object SshKey
  extends Path
  with Listable[SshKey, responses.SshKeys]
  with SshKeyActions {
  override val path: Seq[String] = Seq("account", "keys")

  def setNameByFingerprint(fingerprint: String, name: String)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action.Create] = {
    val path = this.path ++ Seq(fingerprint)
    val value = "name" -> name

    for {
      response <- client.put[responses.SshKey](path, value)
    } yield {
      Action.Create(name, response.sshKey)
    }
  }

  def setNameById(id: BigInt, name: String)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action.Create] = {
    val path = this.path ++ Seq(id.toString)
    val value = "name" -> name

    for {
      response <- client.put[responses.SshKey](path, value)
    } yield {
      Action.Create(name, response.sshKey)
    }
  }

  def create(name: String, publicKey: String)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action.Create] = {
    val value = ("name" -> name) ~
      ("public_key" -> publicKey)

    for {
      response <- client.post[responses.SshKey](path, value)
    } yield {
      Action.Create(name, response.sshKey)
    }
  }

  def deleteById(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action.DeleteById] = {
    val path = this.path ++ Seq(id.toString)
    for {
      () <- client.delete(path)
    } yield Action.DeleteById(id)
  }

  def deleteByFingerprint(fingerprint: String)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Action.DeleteByFingerprint] = {
    val path = this.path ++ Seq(fingerprint)
    for {
      () <- client.delete(path)
    } yield Action.DeleteByFingerprint(fingerprint)
  }

}

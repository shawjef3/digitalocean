package me.jeffshaw.digitalocean

import org.json4s.JsonDSL._

import scala.concurrent.{ExecutionContext, Future}

case class SshKey(
  id: BigInt,
  name: String,
  fingerprint: String,
  publicKey: String
) {
  def setName(name: String)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[SshKey] = {
    SshKey.setNameById(id, name)
  }

  def delete(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    SshKey.deleteById(id)
  }
}

object SshKey 
  extends Path
  with Listable[SshKey, responses.SshKeys] {
  override val path: Seq[String] = Seq("account", "keys")

  def setNameByFingerprint(fingerprint: String, name: String)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[SshKey] = {
    val path = this.path ++ Seq(fingerprint)
    val value = "name" -> name

    for {
      response <- client.put[responses.SshKey](value, path: _*)
    } yield {
      response.sshKey
    }
  }

  def setNameById(id: BigInt, name: String)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[SshKey] = {
    val path = this.path ++ Seq(id.toString)
    val value = "name" -> name

    for {
      response <- client.put[responses.SshKey](value, path: _*)
    } yield {
      response.sshKey
    }
  }

  def create(name: String, publicKey: String)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[SshKey] = {
    val value = ("name" -> name) ~
      ("public_key" -> publicKey)

    for {
      response <- client.post[responses.SshKey](value, path: _*)
    } yield {
      response.sshKey
    }
  }

  def deleteById(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    val path = this.path ++ Seq(id.toString)
    client.delete(path: _*)
  }

  def deleteByFingerprint(fingerprint: String)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    val path = this.path ++ Seq(fingerprint)
    client.delete(path: _*)
  }
}

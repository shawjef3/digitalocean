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

object SshKey {

  def setNameByFingerprint(fingerprint: String, name: String)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[SshKey] = {
    val value = "name" -> name

    client.put[SshKey](value, "account", "keys", fingerprint)
  }

  def setNameById(id: BigInt, name: String)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[SshKey] = {
    val value = "name" -> name

    client.put[SshKey](value, "account", "keys", id.toString())
  }

  def create(name: String, publicKey: String)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[SshKey] = {
    val value = ("name" -> name) ~
      ("public_key" -> publicKey)

    client.post[SshKey](value, "account", "keys")
  }

  def list(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Seq[SshKey]] = {
    client.get[Seq[SshKey]]("account", "keys")
  }

  def deleteById(id: BigInt)(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    client.delete("account", "keys", id.toString)
  }

  def deleteByFingerprint(fingerprint: String)
      (implicit client: DigitalOceanClient, ec: ExecutionContext): Future[Unit] = {
    client.delete("account", "keys", fingerprint)
  }
}

package me.jeffshaw.digitalocean

import java.io.{ByteArrayOutputStream, DataOutputStream}
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPublicKey
import java.util.Base64
import org.scalatest.BeforeAndAfterAll
import scala.concurrent._
import scala.concurrent.duration._
import scala.util.Random

/**
 * Note that these tests randomly fail, because Digital Ocean is slow
 * to update your list of keys that are accessible to the API. This is the
 * reason for all the calls to pollKeys(Vector[SshKey] => Boolean).
 * Sometimes even a 30 second wait isn't enough.
 */
class SshKeySpec extends Suite with BeforeAndAfterAll {
  private def genPK = {
    val kpg = KeyPairGenerator.getInstance("RSA")
    kpg.initialize(512)
    val pc = kpg.genKeyPair.getPublic.asInstanceOf[RSAPublicKey]

    val bytes = new ByteArrayOutputStream
    val out = new DataOutputStream(bytes)
    out.writeInt("ssh-rsa".getBytes.length)
    out.write("ssh-rsa".getBytes)
    out.writeInt(pc.getPublicExponent.toByteArray.length)
    out.write(pc.getPublicExponent.toByteArray)
    out.writeInt(pc.getModulus.toByteArray.length)
    out.write(pc.getModulus.toByteArray)
    val key_val = Base64.getEncoder().encodeToString(bytes.toByteArray)
    s"ssh-rsa $key_val Test Ssh Key"
  }

  val namePrefix = "ScalaTest"

  override protected def afterAll(): Unit = {
    val cleanup = for {
      keys <- SshKey.list()
      deletions <- Future.sequence(keys.filter(_.name.startsWith(namePrefix)).map(_.delete()))
    } yield deletions

    Await.result(cleanup, 10 seconds)
  }

  /**
    * Repeatedly execute SshKey.list() until a predicate is satisfied.
    */
  def pollKeys(predicate: Vector[SshKey] => Boolean): Future[Vector[SshKey]] ={
    for {
      keys <- client.poll[Vector[SshKey]](SshKey.list().map(_.toVector), predicate)
    } yield {
      assert(predicate(keys))
      keys
    }
  }

  test("Ssh keys can be created, renamed, listed, and deleted (by Id).") {
    val name = namePrefix + Random.nextInt()
    val publicKey: String = genPK
    val updatedName = name + "Updated"

    val t = for {
      key <- SshKey.create(name, publicKey)
      keys <- pollKeys(_.contains(key))
      newKey <- SshKey.setNameById(key.id, updatedName)
      keysWithRename <- pollKeys(keys => keys.exists(_.name == updatedName))
      _ = assert(!keysWithRename.exists(_.name == name))
      _ = assert(key == newKey)
      () <- SshKey.deleteById(key.id)
      keysAfterDelete <- pollKeys(! _.exists(_.id == key.id))
    } yield ()

    Await.result(t, 2 minutes)
  }

  test("Ssh keys can be created, renamed, listed, and deleted (by fingerprint).") {
    val name = namePrefix + Random.nextInt()
    val publicKey: String = genPK
    val updatedName = name + "Updated"

    val t = for {
      key <- SshKey.create(name, publicKey)
      keys <- pollKeys(_.contains(key))
      newKey <- SshKey.setNameByFingerprint(key.fingerprint, updatedName)
      keysWithRename <- pollKeys(keys => keys.exists(_.name == updatedName))
      _ = assert(!keysWithRename.exists(_.name == name))
      _ = assert(key == newKey)
      () <- SshKey.deleteByFingerprint(key.fingerprint)
      keysAfterDelete <- pollKeys(! _.exists(_.fingerprint == key.fingerprint))
    } yield ()

    Await.result(t, 2 minutes)
  }

  test("Ssh keys can be created, renamed, and deleted (native).") {
    val name = namePrefix + Random.nextInt()
    val publicKey: String = genPK
    val updatedName = name + "Updated"

    val t = for {
      key <- SshKey.create(name, publicKey)
      keys <- pollKeys(_.contains(key))
      newKey <- key.setName(updatedName)
      keysWithRename <- pollKeys(_.exists(_.name == updatedName))
      _ = assert(!keysWithRename.exists(_.name == name))
      _ = assert(key == newKey)
      () <- newKey.delete()
      keysAfterDelete <- pollKeys(! _.contains(newKey))
    } yield ()

    Await.result(t, 2 minutes)
  }
}

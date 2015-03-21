package me.jeffshaw.digitalocean

import java.io.{DataOutputStream, ByteArrayOutputStream}
import java.security.interfaces.RSAPublicKey
import java.security.KeyPairGenerator
import java.util.Base64

import org.scalatest.{BeforeAndAfterEach, BeforeAndAfterAll}

import scala.concurrent._, duration._
import scala.util.Random

/**
 * Note that these tests randomly fail, because Digital Ocean is slow
 * to update your list of keys that are accessible to the API. This is the
 * reason for all the calls to Thread.sleep(int).
 * Sometimes even a 30 second wait isn't enough.
 */
class SshKeySpec extends Spec with BeforeAndAfterAll with BeforeAndAfterEach {
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

  private def keysCompare(k1: SshKey, k2: SshKey) = (k1.id == k2.id) && (k1.fingerprint == k2.fingerprint) && (k1.publicKey == k2.publicKey)

  val namePrefix = "ScalaTest"

  private val publicKey: String = genPK

  override protected def afterEach(): Unit = {
    val cleanup = for {
      keys <- SshKey.list
      deletions <- Future.sequence(keys.filter(_.name.startsWith(namePrefix)).map(_.delete))
    } yield deletions

    Await.ready(cleanup, 10 seconds)
  }

  test("(randomly fails)Ssh keys can be created, renamed, listed, and deleted (by Id).") {
    val name = namePrefix + Random.nextInt()
    val updatedName = namePrefix + "Updated" + Random.nextInt()

    val key = Await.result(SshKey.create(name, publicKey), 10 seconds)

    var keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(keys.contains(key))

    val newKey = Await.result(SshKey.setNameById(key.id, updatedName), 10 seconds)

    //Give Digital Ocean some time to catch up
    Thread.sleep(10000)

    keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(keys.contains(newKey))
    assert(!keys.contains(key))

    assert(keysCompare(key, newKey))

    Await.result(SshKey.deleteById(key.id), 10 seconds)

    keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(!keys.contains(newKey))
  }

  test("(randomly fails)Ssh keys can be created, renamed, listed, and deleted (by fingerprint).") {
    val name = namePrefix + Random.nextInt()
    val updatedName = namePrefix + "Updated" + Random.nextInt()

    val key = Await.result(SshKey.create(name, publicKey), 10 seconds)
    var keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(keys.contains(key))

    val newKey = Await.result(SshKey.setNameByFingerprint(key.fingerprint, updatedName), 10 seconds)

    //Give Digital Ocean some time to catch up
    Thread.sleep(10000)

    keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(keys.contains(newKey))
    assert(!keys.contains(key))

    assert(keysCompare(key, newKey))

    Await.result(SshKey.deleteByFingerprint(key.fingerprint), 10 seconds)

    keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(!keys.contains(newKey))
  }

  test("(randomly fails)Ssh keys can be created, renamed, and deleted (native).") {
    val name = namePrefix + Random.nextInt()
    val updatedName = namePrefix + "Updated" + Random.nextInt()

    val key = Await.result(SshKey.create(name, publicKey), 10 seconds)
    var keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(keys.contains(key))

    val newKey = Await.result(key.setName(updatedName), 10 seconds)

    //Give Digital Ocean some time to catch up
    Thread.sleep(10000)

    keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(keys.contains(newKey))
    assert(!keys.contains(key))

    assert(keysCompare(key, newKey))

    Await.result(newKey.delete, 10 seconds)

    keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(!keys.contains(newKey))
  }
}

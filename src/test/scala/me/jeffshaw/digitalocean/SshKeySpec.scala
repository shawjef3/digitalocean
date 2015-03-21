package me.jeffshaw.digitalocean

import java.io.{DataOutputStream, ByteArrayOutputStream}
import java.security.interfaces.RSAPublicKey
import java.security.{PublicKey, KeyPair, KeyPairGenerator}
import java.util.Base64

import org.scalatest.{BeforeAndAfterEach, BeforeAndAfterAll, fixture}

import scala.concurrent._, duration._
import scala.util.Random

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

  private var publicKey: String = ""
  private var name1 = ""
  private var name2 = ""

  override protected def beforeAll() = { publicKey = genPK }

  override protected def beforeEach() = {
    name1 = "Test" + Random.nextInt()
    name2 = "TestUpdated" + Random.nextInt()
  }

  test("Ssh keys can be created, renamed, listed, and deleted (by Id).") {
    val key = Await.result(SshKey.create(name1, publicKey), 10 seconds)

    var keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(keys.contains(key))

    val newKey = Await.result(SshKey.setNameById(key.id, name2), 10 seconds)

    keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(keys.contains(newKey))
    assert(!keys.contains(key))

    assert(keysCompare(key, newKey))

    Await.result(SshKey.deleteById(key.id), 10 seconds)

    keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(!keys.contains(newKey))
  }

  test("Ssh keys can be created, renamed, listed, and deleted (by fingerprint).") {
    val key = Await.result(SshKey.create(name1, publicKey), 10 seconds)
    var keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(keys.contains(key))

    val newKey = Await.result(SshKey.setNameByFingerprint(key.fingerprint, name2), 10 seconds)

    keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(keys.contains(newKey))
    assert(!keys.contains(key))

    assert(keysCompare(key, newKey))

    Await.result(SshKey.deleteByFingerprint(key.fingerprint), 10 seconds)

    keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(!keys.contains(newKey))
  }

  test("Ssh keys can be created, renamed, and deleted (native).") {
    val key = Await.result(SshKey.create(name1, publicKey), 10 seconds)
    var keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(keys.contains(key))

    val newKey = Await.result(key.setName(name2), 10 seconds)

    keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(keys.contains(newKey))
    assert(!keys.contains(key))

    assert(keysCompare(key, newKey))

    Await.result(newKey.delete, 10 seconds)

    keys = Await.result(SshKey.list, 10 seconds).toSeq
    assert(!keys.contains(newKey))
  }
}

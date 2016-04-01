package me.jeffshaw.digitalocean

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait SshKeyActions {

  sealed abstract class Action[A](
    completionCheck: Iterator[SshKey] => Boolean,
    value: A
  ) {

    def complete()(implicit client: DigitalOceanClient, ec: ExecutionContext): Future[A] = {
      for {
        i <- SshKey.list()
        success = completionCheck(i)
        x <- if (success) Future.successful(value)
        else Future(Thread.sleep(client.actionCheckInterval.toMillis)).flatMap(_ => complete())
      } yield x
    }

  }

  object Action {

    case class Create(name: String, sshKey: SshKey) extends Action[SshKey](
      i => i.contains(sshKey),
      sshKey
    ) {

      override def hashCode(): Int = {
        sshKey.hashCode()
      }

      override def equals(obj: Any): Boolean = {
        obj match {
          case other: SshKey =>
            sshKey == other
          case other: Create =>
            sshKey == other.sshKey
          case _ =>
            false
        }
      }
    }

    object Create {
      implicit def toSshKey(action: Create): SshKey = {
        action.sshKey
      }
    }

    case class DeleteById(id: BigInt) extends Action[Unit](
      i => !i.exists(_.id == id),
      ()
    ) {
      override def hashCode(): Int = id.hashCode

      override def equals(obj: scala.Any): Boolean = {
        obj match {
          case other: DeleteById =>
            id == other.id
          case _ =>
            false
        }
      }
    }

    case class DeleteByFingerprint(fingerprint: String) extends Action[Unit](
      i => !i.exists(_.fingerprint == fingerprint),
      ()
    ) {
      override def hashCode(): Int = fingerprint.hashCode()

      override def equals(obj: scala.Any): Boolean = {
        obj match {
          case other: DeleteByFingerprint =>
            fingerprint == other.fingerprint
          case _ =>
            false
        }
      }
    }

  }

}

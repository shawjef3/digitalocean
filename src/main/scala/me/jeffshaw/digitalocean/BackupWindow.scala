package me.jeffshaw.digitalocean

import java.time.Instant

case class BackupWindow(
  start: Instant,
  end: Instant
)

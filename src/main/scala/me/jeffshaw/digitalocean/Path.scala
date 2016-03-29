package me.jeffshaw.digitalocean

private[digitalocean] trait Path {
  protected val path: Seq[String]

  protected val queryParameters: Map[String, Seq[String]] = Map.empty
}

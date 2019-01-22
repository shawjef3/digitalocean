package me.jeffshaw.digitalocean.responses

import org.json4s.{CustomSerializer, JValue}

private[digitalocean] abstract class HasBiMapSerializer[A: Manifest] {
  private[digitalocean] val jsonMap: Map[A, JValue]

  private[digitalocean] case object Serializer extends CustomSerializer[A](formats =>
    (HasBiMapSerializer.inverse(jsonMap), jsonMap.asInstanceOf[Map[Any, JValue]])
  )
}

private[digitalocean] object HasBiMapSerializer {
  def inverse[K, V](map: Map[K, V]): Map[V, K] = {
    val duplicateCheck = collection.mutable.Set.empty[V]
    for ((k, v) <- map) yield {
      if (!duplicateCheck.add(v)) {
        throw new IllegalArgumentException(s"duplicate key $v")
      }
      v -> k
    }
  }
}

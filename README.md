digitalocean
============

Scala wrapper around Digital Ocean's API, version 2

Install SBT, clone this repository, and cd to it.


```scala
sbt

console

import scala.concurrent._, duration._, ExecutionContext.Implicits._
import me.jeffshaw.digitalocean._
implicit val client = DigitalOceanClient("api_key")

//List all the regions.
Await.result(Region.list, 5 seconds)

//Create a small CentOS 6.5 32-bit droplet.
val droplet = Await.result(Droplet.create(name, region, size, image, Seq.empty, false, false, false, None), 10 seconds)

//destroy it
droplet.delete
```

To run tests, set your api token in src/test/resources/application.conf, and then run test in the sbt console.
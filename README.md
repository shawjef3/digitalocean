digitalocean
============

Scala wrapper around Digital Ocean's API, version 2

Install SBT, clone this repository, and cd to it.


```scala
sbt

console
//use :paste if you want to copy-paste the following.

import scala.concurrent._, duration._, ExecutionContext.Implicits._
import me.jeffshaw.digitalocean._
implicit val client = DigitalOceanClient("api_token")

//List all the regions.
Await.result(Region.list, 5 seconds)

//Create a small CentOS 6.5 32-bit droplet.
val droplet = Await.result(Droplet.create("test", NewYork2, `512mb`, 3448674, Seq.empty, false, false, false, None), 10 seconds)

//Destroy it after it becomes active.
while (Await.result(Droplet(droplet.id), 10 seconds).status != Active) {
    println(s"waiting for status = active for droplet ${droplet.id}")
    Thread.sleep(5 * 1000)
}

//Wait a few seconds, or else the delete will probably fail.
Thread.sleep(10 * 1000)

Await.result(droplet.delete, 10 seconds)

//CTRL-D if you used :paste.
```

To run tests, set your api token in src/test/resources/application.conf, and then run test in the sbt console.

Note that this library currently does not support pagination, so all calls will only ever give the first page sent from Digital Ocean.

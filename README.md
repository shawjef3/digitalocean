digitalocean
============

Scala wrapper around Digital Ocean's API, version 2

Note that except some await member functions that are meant specifically for waiting for an action to complete or droplet to be created, this API is entirely asynchronous, so you'll want to know how to use Futures.

Install SBT, clone this repository, and cd to it.


```scala
sbt

console
//use :paste if you want to copy-paste the following, but be sure to set your api token first.

import scala.concurrent._, duration._, ExecutionContext.Implicits._
import me.jeffshaw.digitalocean._

implicit val client = DigitalOceanClient("api token")

//List all the regions.
Await.result(Region.list, 5 seconds)

//Create a small CentOS 6.5 32-bit droplet.
val droplet = Await.result(Droplet.create("test", NewYork2, `512mb`, 3448674, Seq.empty, false, false, false, None), 10 seconds)

//Wait for the droplet to become active.
droplet.await

//Do stuff with the droplet.

//Run the delete the command, and then wait for the droplet to stop existing.

Await.result(droplet.delete, 5 seconds).await

//CTRL-D if you used :paste.
```

To run tests, set your api token in src/test/resources/application.conf, and then run test in the sbt console.

As of version 0.2, this library supports pagination.

digitalocean
============

Scala wrapper around Digital Ocean's API, version 2

This API is entirely asynchronous, so you'll want to know how to use Futures. Some classes have a "complete" method that
perform several operations for you so that you can know if an operation such as Droplet.create or an action have
completed. "complete" methods are also futures.

##Instructions

Install SBT, clone this repository, and cd to it.


```scala
sbt

console
//use :paste if you want to copy-paste the following, but be sure to set your api token first.

import scala.concurrent._, duration._, ExecutionContext.Implicits._
import me.jeffshaw.digitalocean._

implicit val client = DigitalOceanClient(
  token = "",
  maxWaitPerRequest = 5 seconds,
  //The following is used for polling action completion.
  actionCheckInterval = 15 seconds
)

//List all the regions.
val regions = Await.result(Region.list, 5 seconds)

//Create a small CentOS 6.5 32-bit droplet.
val droplet = Await.result(Droplet.create("test", NewYork2, `512mb`, 3448674, Seq.empty, false, false, false, None), 10 seconds)

//Wait for the droplet to become active.
Await.result(droplet.complete, 2 minutes)

//Do stuff with the droplet.

//Run the delete the command, and then wait for the droplet to stop existing.

Await.result(droplet.delete.flatMap(_.complete), 2 minutes)

//CTRL-D if you used :paste.
```

To run tests, set your api token in src/test/resources/application.conf, and then run test in the sbt console.

##Changelog

0.5
DNS functionality added to the dns package.

0.2
List functions now return iterators that support paged responses.

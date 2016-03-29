digitalocean
============

Scala wrapper around Digital Ocean's API, version 2

This API is entirely asynchronous, so you'll want to know how to use Futures. Some classes have a "complete" method that
perform several operations for you so that you can know if an operation such as Droplet.create or an action have
completed. "complete" methods also return futures.

##Scaladoc

http://www.jeffshaw.me/digitalocean/

##Instructions

###Dependency

This project is now in Maven Central for Scala 2.10 and 2.11. You can add it to your dependencies in your project's sbt file.

```scala
libraryDependencies += "me.jeffshaw" %% "digitalocean" % "2.0"
```

Or, for a maven project:

```xml
<dependency>
  <groupId>me.jeffshaw</groupId>
  <artifactId>digitalocean_2.11</artifactId>
  <version>1.1</version>
</dependency>
```

###Local Compilation

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
val droplet =
  Await.result(
    Droplet.create(
      name = "test",
      region = NewYork2,
      size = `512mb`,
      image = 11523060,
      sshKeys = Seq.empty,
      backups = false,
      ipv6 = false,
      privateNetworking = false,
      userData = None
    ),
    atMost = 10 seconds
  )

//Wait for the droplet to become active.
Await.result(droplet.complete, 2 minutes)

//Do stuff with the droplet.

//Run the delete the command, and then wait for the droplet to stop existing.

Await.result(droplet.delete.flatMap(_.complete), 2 minutes)

//CTRL-D if you used :paste.
```

To run tests, set your api token in src/test/resources/application.conf, and then run test in the sbt console.

##Changelog

### 2.0
* Methods that return Futures but didn't have an empty parameter list now require an empty parameter list. Examples are list methods and delete methods.
* Image list methods now ask whether you want private images or not, and if you want All, Application, or Distribution images.
* Add support for floating IPs.
* Add support for actions on droplet tags.
* The ssh key tests are more reliable.
* Supports the latest droplet metadata.

### 1.1
* Add Toronto1 to the Region enumeration.

### 1.0
* Add Frankfurt1 to the Region enumeration.
* OAuth support may come in a future 2.0 release.

### 0.8
* Update to account for [March 20 changes](https://assets.digitalocean.com/email/APIv2_Breaking_Changes_Email.html).
* Fix SSH key functionality, and added tests for SshKey thanks to [bass3t](https://github.com/bass3t).

### 0.7
Droplet creation has changed to return less information about the droplet.
My solution is to just read the ID, and then ask for all the information
in a second request.

### 0.6
Metadata functionality added to the metadata package.

### 0.5
DNS functionality added to the dns package.

### 0.2
List functions now return iterators that support paged responses.

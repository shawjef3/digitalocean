name := "digitalocean"

organization := "me.jeffshaw"

version := "6.1"

libraryDependencies ++= Seq(
  "org.asynchttpclient" % "async-http-client" % "2.1.0",
  "org.json4s" %% "json4s-native" % "3.5.3",
  "com.typesafe" % "config" % "1.3.2" % "test",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.9.0" % "test",
  "org.apache.logging.log4j" % "log4j-api" % "2.9.0" % "test",
  "org.apache.logging.log4j" % "log4j-core" % "2.9.0" % "test"
)

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.10.7")

mimaPreviousArtifacts := Set("me.jeffshaw" %% "digitalocean" % "6.0")

parallelExecution := false

licenses := Seq("Apache License, Version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/shawjef3/digitalocean"))

pomExtra := (
  <developers>
    <developer>
      <name>Jeff Shaw</name>
      <id>shawjef3</id>
    </developer>
    <developer>
      <name>bass3t</name>
      <id>bass3t</id>
    </developer>
    <developer>
      <name>flavienbert</name>
      <id>flavienbert</id>
    </developer>
  </developers>
  <scm>
    <url>git@github.com:shawjef3/digitalocean.git</url>
    <connection>scm:git:git@github.com:shawjef3/digitalocean.git</connection>
  </scm>
)

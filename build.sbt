name := "digitalocean"

organization := "me.jeffshaw"

version := "4.0"

libraryDependencies ++= Seq(
  "org.asynchttpclient" % "async-http-client" % "2.0.24",
  "org.json4s" %% "json4s-native" % "3.5.0",
  "com.typesafe" % "config" % "1.3.1" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

scalaVersion := "2.12.0"

crossScalaVersions := Seq("2.11.8", "2.10.6")

mimaPreviousArtifacts := Set("me.jeffshaw" %% "digitalocean" % "3.0")

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

name := "digitalocean"

organization := "me.jeffshaw"

version := "1.0"

libraryDependencies ++= Seq(
	"net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
	"org.json4s" %% "json4s-native" % "3.2.11",
	"com.typesafe" % "config" % "1.2.1" % "test",
	"org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

scalaVersion := "2.11.5"

crossScalaVersions := Seq("2.10.5")

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
  </developers>
  <scm>
    <url>git@github.com:shawjef3/digitalocean.git</url>
    <connection>scm:git:git@github.com:shawjef3/digitalocean.git</connection>
  </scm>
)

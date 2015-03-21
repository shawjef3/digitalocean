//For dependency graphs.
//https://github.com/jrudolph/sbt-dependency-graph
net.virtualvoid.sbt.graph.Plugin.graphSettings

name := "digitalocean"

organization := "me.jeffshaw"

version := "0.8"

libraryDependencies ++= Seq(
	"net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
	"org.json4s" %% "json4s-native" % "3.2.11",
	"com.typesafe" % "config" % "1.2.1" % "test",
	"org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

scalaVersion := "2.11.5"

crossScalaVersions := Seq("2.10.5")

publishMavenStyle := true

publishTo := {
  val nexus = "http://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

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

pomIncludeRepository := { _ => false }

xerial.sbt.Sonatype.sonatypeSettings

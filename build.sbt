//For dependency graphs.
//https://github.com/jrudolph/sbt-dependency-graph
net.virtualvoid.sbt.graph.Plugin.graphSettings

name := "digitalocean"

organization := "me.jeffshaw"

version := "0.4"

libraryDependencies ++= Seq(
	"net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
	"org.json4s" %% "json4s-native" % "3.2.10",
	"com.typesafe" % "config" % "1.2.1" % "test",
	"org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

scalaVersion := "2.11.2"

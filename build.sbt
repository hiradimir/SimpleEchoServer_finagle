organization := "com.yutagithub"

name := "SimpleEchoServer_finagle"

version := "0.1.0"

scalaVersion := "2.9.1"

resolvers += "twitter-repo" at "http://maven.twttr.com"

libraryDependencies ++= Seq(
  "com.twitter" % "finagle-http_2.9.1" % "2.0.0"
)

EclipseKeys.withSource := true


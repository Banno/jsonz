name := "jsonz"

organization := "jsonz"

version := "0.6-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

resolvers ++= Seq(
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases",
  "spray.io" at "http://repo.spray.io/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Coda Hale" at "http://repo.codahale.com"
)

publishTo := Some("Banno Snapshots Repo" at "http://nexus.banno.com/nexus/content/repositories/snapshots")

credentials += Credentials(Path.userHome / ".ivy2" / ".banno_credentials")

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.0.0",
  "org.scalaz" %% "scalaz-typelevel" % "7.0.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.1.3"
)

// joda datetime support
libraryDependencies ++= Seq(
  "org.joda" % "joda-convert" % "1.1",
  "joda-time" % "joda-time" % "2.0"
)

// spray support
libraryDependencies ++= Seq(
  "io.spray" % "spray-httpx" % "1.1-RC3" % "provided",
  "com.typesafe.akka" %% "akka-actor" % "2.1.4" % "provided"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.12.3" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.1" % "test"
)

// scalacOptions += "-Xlog-implicits"

name := "jsonz"

organization := "jsonz"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.11.1", "2.10.4")

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

resolvers ++= Seq(
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases",
  "spray.io" at "http://repo.spray.io/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)

publishTo := Some("Banno Snapshots Repo" at "http://nexus.banno.com/nexus/content/repositories/snapshots")

credentials += Credentials(Path.userHome / ".ivy2" / ".banno_credentials")

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.0",
  "org.scalaz" %% "scalaz-typelevel" % "7.1.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.1",
  "com.typesafe.akka" %% "akka-actor" % "2.3.4" % "provided"
)

// joda datetime support
libraryDependencies ++= Seq(
  "org.joda" % "joda-convert" % "1.1" % "provided",
  "joda-time" % "joda-time" % "2.0" % "provided"
)

// spray support
libraryDependencies ++= {
  if (scalaVersion.value.startsWith("2.11"))
    Seq(
      "io.spray" % "spray-httpx" % "1.3.1" % "provided"
    )
  else
    Seq(
      "io.spray" % "spray-httpx" % "1.1.1" % "provided"
    )
}

// specs2 support
libraryDependencies += "org.specs2" %% "specs2" % "2.4.2" % "provided"

// test deps
libraryDependencies ++=
  Seq(
    "org.scalacheck" %% "scalacheck" % "1.11.5" % "test",
    "org.typelevel" %% "scalaz-specs2" % "0.3.0" % "test",
    "org.scalaz" %% "scalaz-scalacheck-binding" % "7.1.0" % "test"
  )

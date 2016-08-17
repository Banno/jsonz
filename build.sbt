name := "jsonz"

organization := "jsonz"

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.10.6", "2.11.8")

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

resolvers ++= Seq(
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases",
  "spray.io" at "http://repo.spray.io/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)

bintrayOrganization := Some("banno")

bintrayRepository := "oss"

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

publishTo := {
  if (version.value.trim.endsWith("SNAPSHOT")) {
    Some("Banno Snapshots Repo" at "http://nexus.banno.com/nexus/content/repositories/snapshots")
  } else {
    (publishTo in bintray).value
  }
}

releaseCrossBuild := true

credentials += Credentials(Path.userHome / ".ivy2" / ".banno_credentials")

val jacksonVersion = "2.6.7"
val scalazVersion = "7.2.5"
val akkaVersion = "2.3.15"

val scalaCheckVersion = "1.13.2"
val specs2Version = "3.8.4"

// required deps
libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % scalazVersion,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion % "provided"
)

// joda datetime support
libraryDependencies ++= Seq(
  "org.joda" % "joda-convert" % "1.8.1" % "provided",
  "joda-time" % "joda-time" % "2.9.4" % "provided"
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
libraryDependencies += "org.specs2" %% "specs2-core" % specs2Version % "provided"

libraryDependencies ++=
  Seq(
    "org.scalacheck" %% "scalacheck" % scalaCheckVersion % "test",
    "org.specs2" %% "specs2-scalacheck" % specs2Version % "test",
    "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion % "test"
  )

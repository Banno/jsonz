name := "jsonz"

organization := "jsonz"

resolvers += "Coda Hale" at "http://repo.codahale.com"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  "com.codahale" %% "jerkson" % "0.5.0",
  "org.scalaz" %% "scalaz-core" % "7.0-SNAPSHOT"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.10" % "test",
  "org.scalacheck" %% "scalacheck" % "1.9" % "test"
)

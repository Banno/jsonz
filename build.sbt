name := "jsonz"

organization := "jsonz"

version := "0.3-SNAPSHOT"

crossScalaVersions := Seq("2.9.1", "2.9.2")

resolvers ++= Seq(
  "Coda Hale" at "http://repo.codahale.com",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases"
)

publishTo := Some("Banno Snapshots Repo" at "http://nexus.banno.com/nexus/content/repositories/snapshots")

credentials += Credentials(Path.userHome / ".ivy2" / ".banno_credentials")

libraryDependencies ++= Seq(
  "com.codahale" % "jerkson_2.9.1" % "0.5.0",
  "org.scalaz" % "scalaz-core_2.9.2" % "7.0.0-M3",
  "org.scalaz" % "scalaz-typelevel_2.9.2" % "7.0.0-M3"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.10" % "test",
  "org.scalacheck" %% "scalacheck" % "1.9" % "test"
)

// scalacOptions += "-Xlog-implicits"

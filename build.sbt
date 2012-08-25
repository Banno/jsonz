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
  "org.scalaz" % "scalaz-core_2.9.2" % "7.0.0-M3"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.10" % "test",
  "org.scalacheck" %% "scalacheck" % "1.9" % "test"
)

libraryDependencies ++= Seq(
  "com.google.caliper" % "caliper" % "0.5-rc1",
  "net.debasishg" % "sjson_2.9.1" % "0.15" % "test"
)

fork in run := true

// from https://github.com/sirthias/scala-benchmarking-template/blob/master/project/Build.scala
onLoad in Global ~= { previous => state =>
  previous {
    state.get(AttributeKey[Boolean]("javaOptionsPatched")) match {
      case None =>
        // get the test classpath, turn into a colon-delimited string
        val classPath = Project.runTask(fullClasspath in Test, state).get._2.toEither.right.get.files.mkString(":")
        // return a state with javaOptionsPatched = true and javaOptions set correctly
        Project.extract(state).append(Seq(javaOptions in run ++= Seq("-cp", classPath)), state.put(AttributeKey[Boolean]("javaOptionsPatched"), true))
      case Some(_) => state // the javaOptions are already patched
    }
  }
}

// scalacOptions += "-Xlog-implicits"

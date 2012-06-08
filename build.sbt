name := "jsonz"

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

libraryDependencies ++= Seq(
  "com.google.caliper" % "caliper" % "0.5-rc1",
  "net.debasishg" %% "sjson" % "0.15" % "test"
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

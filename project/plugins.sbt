resolvers += Resolver.url(
  "bintray-sbt-plugin-releases",
  url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(
  Resolver.ivyStylePatterns)

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")

// https://github.com/sbt/sbt-release/releases
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")

// https://github.com/jrudolph/sbt-dependency-graph/releases
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

// https://github.com/rtimush/sbt-updates/releases
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.2.0")

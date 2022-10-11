name := """Bitter"""



lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """Bitter""",
    organization := "com.example",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.12.2",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
      "com.typesafe.play" %% "play-slick" % "5.0.2",
      "com.typesafe.play" %% "play-slick-evolutions" % "5.0.2",
      "com.h2database" % "h2" % "2.1.214",
      "net.codingwell" %% "scala-guice" % "5.1.0",
      specs2 % Test
    ),
    resolvers ++= Seq(
      "Atlassian Releases" at "https://maven.atlassian.com/public/",
      "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
      Resolver.sonatypeRepo("snapshots"),
      Resolver.jcenterRepo
    )
  )

libraryDependencies += "io.github.honeycomb-cheesecake" %% "play-silhouette" % "7.0.7"
libraryDependencies += "io.github.honeycomb-cheesecake" %% "play-silhouette-cas" % "7.0.6"
libraryDependencies += "io.github.honeycomb-cheesecake" %% "play-silhouette-crypto-jca" % "7.0.7"
libraryDependencies += "io.github.honeycomb-cheesecake" %% "play-silhouette-password-argon2" % "7.0.7"
libraryDependencies += "io.github.honeycomb-cheesecake" %% "play-silhouette-password-bcrypt" % "7.0.7"
libraryDependencies += "io.github.honeycomb-cheesecake" %% "play-silhouette-persistence" % "7.0.7"
libraryDependencies += "io.github.honeycomb-cheesecake" %% "play-silhouette-totp" % "7.0.7"
libraryDependencies += "io.github.honeycomb-cheesecake" %% "play-silhouette-testkit" % "7.0.7" % Test








// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"





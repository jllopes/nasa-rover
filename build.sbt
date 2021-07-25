lazy val root = (project in file("."))
  .settings(
    organization := "io.github.jllopes",
    name := "nasa-rover",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.4",
    libraryDependencies ++= Seq(
      Dependencies.blazeServer,
      Dependencies.blazeClient,
      Dependencies.http4sCirce,
      Dependencies.http4sDsl,
      Dependencies.circe,
      Dependencies.munit,
      Dependencies.munitCatsEffect,
      Dependencies.logback,
      Dependencies.svmSubs,
      Dependencies.scalaLogging
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.0" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )

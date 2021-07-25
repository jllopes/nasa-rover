import sbt._

object Dependencies {
  val Http4sVersion = "0.21.24"
  val CirceVersion = "0.13.0"
  val MunitVersion = "0.7.20"
  val LogbackVersion = "1.2.3"
  val MunitCatsEffectVersion = "0.13.0"
  val scalaLoggingVersion = "3.9.4"

  val blazeServer = "org.http4s" %% "http4s-blaze-server" % Http4sVersion
  val blazeClient = "org.http4s" %% "http4s-blaze-client" % Http4sVersion
  val http4sCirce = "org.http4s" %% "http4s-circe" % Http4sVersion
  val http4sDsl = "org.http4s" %% "http4s-dsl" % Http4sVersion
  val circe = "io.circe" %% "circe-generic" % CirceVersion
  val munit = "org.scalameta" %% "munit" % MunitVersion % Test
  val munitCatsEffect = "org.typelevel" %% "munit-cats-effect-2" % MunitCatsEffectVersion % Test
  val logback = "ch.qos.logback" % "logback-classic" % LogbackVersion
  val svmSubs = "org.scalameta" %% "svm-subs" % "20.2.0"
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
}

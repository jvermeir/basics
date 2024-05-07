name := "scalaVersion"

version := "1.0"

scalaVersion := s"2.13.12"

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

lazy val akkaVersion = "2.9.2"
val AkkaHttpVersion = "10.6.2"

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

javaOptions += "--add-opens=java.base/java.lang=ALL-UNNAMED"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.13",
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  "mysql" % "mysql-connector-java" % "8.0.33",
  "org.scalikejdbc" %% "scalikejdbc" % "4.2.1",
  "org.scalikejdbc" %% "scalikejdbc-config" % "4.2.1",

  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % "test",
  "org.scalatest" %% "scalatest-funsuite" % "3.2.18" % "test",
  "org.scalatest" %% "scalatest" % "3.2.18" % "test",
  "com.h2database" % "h2" % "1.4.200"
)
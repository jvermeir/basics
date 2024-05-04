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

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.13",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.slick" %% "slick" % "3.5.1",
 "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion
)

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"       % "4.2.1",
  "com.h2database"  %  "h2"                % "1.4.200"
)
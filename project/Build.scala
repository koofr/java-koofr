import sbt._
import Keys._

object SdkBuild extends Build {

  override lazy val settings = super.settings ++ Seq(
    organization := "net.koofr",
    version := "0.1-SNAPSHOT",
    resolvers += "restlet" at "http://maven.restlet.org",
    autoScalaLibrary := false,
    crossPaths := false
  )

  lazy val sdk = Project(
    id = "sdk-java",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      libraryDependencies ++= Seq(
        "org.apache.httpcomponents" % "httpclient" % "4.2.1",
        "org.restlet.jse" % "org.restlet.ext.jackson" % "2.1.2",
        "org.restlet.jse" % "org.restlet.ext.httpclient" % "2.1.2"
      )
    )
  )

  lazy val exampleCmdline = Project(
    id = "cmdline",
    base = file("examples/cmdline")
  ) dependsOn sdk

  lazy val exampleInfo = Project(
    id = "info",
    base = file("examples/info")
  ) dependsOn sdk
}
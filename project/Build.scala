import sbt._
import Keys._

object SdkBuild extends Build {

  override lazy val settings = super.settings ++ Seq(
    organization := "net.koofr",
    version := "0.1",
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
      ),
      publishMavenStyle := true,
      publishTo <<= isSnapshot { isSnapshot =>
        val nexus = "https://oss.sonatype.org/"
        if (isSnapshot)
          Some("snapshots" at nexus + "content/repositories/snapshots")
        else
          Some("releases"  at nexus + "service/local/staging/deploy/maven2")
      },
      publishArtifact in Test := false,
      pomExtra :=
        <url>https://github.com/koofr/java-koofr</url>
          <licenses>
            <license>
              <name>MIT</name>
              <url>http://opensource.org/licenses/MIT</url>
              <distribution>repo</distribution>
            </license>
          </licenses>
          <scm>
            <url>https://github.com/koofr/java-koofr.git</url>
          </scm>
          <developers>
            <developer>
              <id>jaKa</id>
              <name>Jaka Mocnik</name>
            </developer>
            <developer>
              <id>edofic</id>
              <name>Andraz Bajt</name>
            </developer>
          </developers>
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
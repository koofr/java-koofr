organization := "net.koofr"

name := "java-koofr"

version := "3.2.0"

autoScalaLibrary := false

crossPaths := false

lazy val sdk = project in file(".")

javacOptions ++= Seq("-source", "1.6")

javacOptions in compile ++= Seq("-target", "1.6")

seq(bintraySettings:_*)

bintray.Keys.bintrayOrganization in bintray.Keys.bintray := Some("koofr")

licenses ++= Seq("MIT" -> url("http://opensource.org/licenses/MIT"))

publishArtifact in Test := false

pomExtra :=
  <url>https://github.com/koofr/java-koofr</url>
    <scm>
      <url>https://github.com/koofr/java-koofr.git</url>
    </scm>
    <developers>
      <developer>
        <id>jkmcnk</id>
        <name>Jaka Mocnik</name>
      </developer>
      <developer>
        <id>edofic</id>
        <name>Andraz Bajt</name>
      </developer>
    </developers>

libraryDependencies ++= Seq(
  "com.eclipsesource.minimal-json" % "minimal-json" % "0.9.4"
)

lazy val exampleCmdline = Project(
  id = "cmdline",
  base = file("examples/cmdline"),
  settings = Project.defaultSettings ++ Seq(mainClass in (Compile,run) := Some("cmdline.Main"))
) dependsOn sdk

lazy val exampleInfo = Project(
  id = "info",
  base = file("examples/info"),
  settings = Project.defaultSettings ++ Seq(mainClass in (Compile,run) := Some("info.Main"))
) dependsOn sdk

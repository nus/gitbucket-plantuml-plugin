name := "gitbucket-plantuml-plugin"

organization := "com.yotaichino"

version := "1.8.0"

scalaVersion := "2.13.18"
gitbucketVersion := "4.48.0"

libraryDependencies ++= Seq(
  "org.scalatest"            %% "scalatest"          % "3.0.8" % "test"
)

scalacOptions := Seq("-deprecation", "-feature", "-language:postfixOps", "-Ydelambdafy:method", "-target:jvm-17")
Compile / javacOptions ++= Seq("-target", "17", "-source", "17")

useJCenter := true 

lazy val downloadPlantuml = taskKey[Unit]("Download the PlantUML ASL Version.")
downloadPlantuml := {
  val url = "https://github.com/plantuml/plantuml/releases/download/v1.2026.10/plantuml-asl-1.2026.10.jar"
  val plantumlJar = new File("lib/plantuml.jar")
  IO.createDirectory(plantumlJar.getParentFile)
  println(s"Downloading PlantUML ASL 1.2026.10 from $url")
  val input = new URL(url).openStream()
  try {
    java.nio.file.Files.copy(
      input,
      plantumlJar.toPath,
      java.nio.file.StandardCopyOption.REPLACE_EXISTING
    )
  } finally {
    input.close()
  }
}

name := "gitbucket-plantuml-plugin"

organization := "com.yotaichino"

version := "1.6.2"

scalaVersion := "2.13.0"
gitbucketVersion := "4.32.0"

libraryDependencies ++= Seq(
  "org.scalatest"            %% "scalatest"          % "3.0.8" % "test"
)

scalacOptions := Seq("-deprecation", "-feature", "-language:postfixOps", "-Ydelambdafy:method", "-target:jvm-1.8")
javacOptions in compile ++= Seq("-target", "8", "-source", "8")

useJCenter := true 

lazy val downloadPlantuml = taskKey[Unit]("Download the PlantUML ASL Version.")
downloadPlantuml := {
  val url = "https://downloads.sourceforge.net/project/plantuml/1.2022.5/plantuml-jar-asl-1.2022.5.zip"
  if (java.nio.file.Files.notExists(new File("lib/plantuml.jar").toPath())) {
    println(url)
    IO.unzipURL(new URL(url), new File("lib"))
  }
}

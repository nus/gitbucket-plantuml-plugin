name := "gitbucket-plantuml-plugin"

organization := "com.yotaichino"

version := "1.3.0"

scalaVersion := "2.12.6"
gitbucketVersion := "4.25.0"

libraryDependencies ++= Seq(
  "org.scalatest"            %% "scalatest"          % "3.0.1" % "test"
)

scalacOptions := Seq("-deprecation", "-feature", "-language:postfixOps", "-Ydelambdafy:method", "-target:jvm-1.8")
javacOptions in compile ++= Seq("-target", "8", "-source", "8")

useJCenter := true 

lazy val downloadPlantuml = taskKey[Unit]("Download the PlantUML ASL Version.")
downloadPlantuml := {
  val url = "https://excellmedia.dl.sourceforge.net/project/plantuml/1.2018.7/plantuml-jar-asl-1.2018.7.zip"
  if (java.nio.file.Files.notExists(new File("lib/plantuml.jar").toPath())) {
    println(url)
    IO.unzipURL(new URL(url), new File("lib"))
  }
}

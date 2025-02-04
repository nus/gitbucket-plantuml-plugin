name := "gitbucket-plantuml-plugin"

organization := "com.yotaichino"

version := "1.8.0"

scalaVersion := "2.13.7"
gitbucketVersion := "4.36.0"

libraryDependencies ++= Seq(
  "org.scalatest"            %% "scalatest"          % "3.0.8" % "test"
)

libraryDependencies += "net.sourceforge.plantuml" % "plantuml-asl" % "1.2025.0"

scalacOptions := Seq("-deprecation", "-feature", "-language:postfixOps", "-Ydelambdafy:method", "-target:jvm-1.8")
Compile / javacOptions ++= Seq("-target", "8", "-source", "17")

useJCenter := true

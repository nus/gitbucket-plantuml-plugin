name := "gitbucket-plantuml-plugin"

organization := "com.yotaichino"

version := "1.8.0"

scalaVersion := "2.13.18"
gitbucketVersion := "4.47.0"

libraryDependencies ++= Seq(
  "net.sourceforge.plantuml" % "plantuml-asl" % "1.2026.8",
  "org.scalatest"            %% "scalatest"   % "3.0.8" % "test"
)

scalacOptions := Seq("-deprecation", "-feature", "-language:postfixOps", "-Ydelambdafy:method", "-release:17")
Compile / javacOptions ++= Seq("-source", "17", "-target", "17")

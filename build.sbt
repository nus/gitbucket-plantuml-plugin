name := "gitbucket-plantuml-plugin"

organization := "com.yotaichino"

version := "1.4.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "io.github.gitbucket"      %% "gitbucket"          % "4.16.0"   % "provided",
  "javax.servlet"            % "javax.servlet-api"   % "3.1.0" % "provided",
  "org.scalatest"            %% "scalatest"          % "3.0.1" % "test"
)

scalacOptions := Seq("-deprecation", "-feature", "-language:postfixOps", "-Ydelambdafy:method", "-target:jvm-1.8")
javacOptions in compile ++= Seq("-target", "8", "-source", "8")

useJCenter := true

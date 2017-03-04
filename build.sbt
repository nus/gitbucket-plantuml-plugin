name := "gitbucket-plantuml-plugin"

organization := "com.yotaichino"

version := "1.0.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "io.github.gitbucket"      %% "gitbucket"          % "4.8"   % "provided",
  "javax.servlet"            % "javax.servlet-api"   % "3.1.0" % "provided"
)

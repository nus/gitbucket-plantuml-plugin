name := "gitbucket-plantuml-plugin"

organization := "com.yotaichino"

version := "1.1.3"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "io.github.gitbucket"      %% "gitbucket"          % "4.12.1"   % "provided",
  "javax.servlet"            % "javax.servlet-api"   % "3.1.0" % "provided"
)

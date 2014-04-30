name := "geggg-scala-http"

version := "0.1"

scalaVersion := "2.11.0"

libraryDependencies ++= Seq(
    "junit" % "junit" % "4.11" % "test"
    ,"org.scalatest" %% "scalatest" % "2.1.3" % "test"
    ,"com.github.kristofa" % "mock-http-server" % "4.0"
)

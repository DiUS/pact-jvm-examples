name := "javascript-consumer"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.dius" %% "pact-consumer-jvm" % "1.0"
)     

play.Project.playScalaSettings

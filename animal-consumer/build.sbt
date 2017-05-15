name := "animal-consumer"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "au.com.dius" %% "pact-consumer-jvm" % "1.0"
)     

play.Project.playScalaSettings

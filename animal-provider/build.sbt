name := "animal-provider"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)     

play.Project.playScalaSettings

PactJvmPlugin.pactSettings

net.virtualvoid.sbt.graph.Plugin.graphSettings
name := "javascript-consumer"

version := "1.0-SNAPSHOT"

libraryDependencies += "au.com.dius" %% "pact-jvm-consumer" % "1.4" % "test"

libraryDependencies += "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.0.4" % "test"

play.Project.playScalaSettings

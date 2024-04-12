

ThisBuild / scalaVersion := "2.13.13"

lazy val ktableIntro = (project in file("."))
  .settings(
    name := "ktableIntro",
    libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "2.4.0",
    libraryDependencies  += "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.6"
  )
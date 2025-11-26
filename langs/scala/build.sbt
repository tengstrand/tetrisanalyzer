name := "Tetris Analyzer"
version := "2.0"
organization := "nu.tengstrand"
scalaVersion := "3.3.3"

// Main class for running the application
Compile / run / mainClass := Some("nu.tengstrand.tetrisanalyzer.gui.TetrisAnalyzer")

// Assembly configuration
assembly / assemblyJarName := "TetrisAnalyzer-2.0.jar"
assembly / mainClass := Some("nu.tengstrand.tetrisanalyzer.gui.TetrisAnalyzer")

// Dependencies for Scala 3
libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
  "junit" % "junit" % "4.13.2" % "test",
  "org.scalatest" %% "scalatest" % "3.2.17" % "test",
  "org.scalatestplus" %% "junit-4-13" % "3.2.17.0" % "test"
)

// Scala 3 compiler options
scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked"
)

// Exclude META-INF files in assembly (to avoid conflicts)
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}


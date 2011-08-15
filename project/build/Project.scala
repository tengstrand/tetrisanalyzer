import sbt._

class ExecutableProject(info: ProjectInfo) extends DefaultProject(info) with AssemblyProject {
  override def mainClass = Some("nu.tengstrand.tetrisanalyzer.gui.TetrisAnalyzer")
}

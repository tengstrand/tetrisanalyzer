package nu.tengstrand.tetrisanalyzer.gui

import scala.swing._
import scala.swing.Swing
import nu.tengstrand.tetrisanalyzer.game._
import nu.tengstrand.tetrisanalyzer.game.keyevent.MainKeyReceiver
import nu.tengstrand.tetrisanalyzer.settings.DefaultColorSettings

object TetrisAnalyzer {
  val Version = "2.0"

  def main(args: Array[String]): Unit = {
    Swing.onEDT {
      val frame = new TetrisAnalyzerFrame(Version)
      frame.visible = true
    }
  }
}

private class TetrisAnalyzerFrame(version: String) extends MainFrame {
  title = s"Tetris Analyzer $version - by Joakim Tengstrand"

  preferredSize = new Dimension(850, 560)

  private val colorSettings = new DefaultColorSettings
  private val gameView = new GameView(colorSettings)
  private val timer = new Timer(this, gameView)
  private val game = new Game(timer, gameView)

  private val timerThread = new Thread(() => timer.start())
  timerThread.setDaemon(true)
  timerThread.start()

  contents = new BoxPanel(Orientation.Horizontal) {
    contents += gameView
  }

  new MainKeyReceiver(game, gameView)
}
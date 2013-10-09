package nu.tengstrand.tetrisanalyzer.gui

import scala.swing._
import nu.tengstrand.tetrisanalyzer.game._
import nu.tengstrand.tetrisanalyzer.game.keyevent.MainKeyReceiver
import actors.Actor._
import nu.tengstrand.tetrisanalyzer.settings.DefaultColorSettings

object TetrisAnalyzer extends SimpleSwingApplication {
  val version = "2.0"

  def top = new MainFrame {
    title = "Tetris Analyzer " + version + " - by Joakim Tengstrand"

    preferredSize = new Dimension(850,560)

    val colorSettings = new DefaultColorSettings
    val gameView = new GameView(colorSettings)
    val timer = new Timer(this, gameView)
    val game = new Game(timer, gameView)

    actor {
      timer.start()
    }

    contents = new BoxPanel(Orientation.Horizontal) {
      contents += gameView
    }

    new MainKeyReceiver(game, gameView)
  }
}
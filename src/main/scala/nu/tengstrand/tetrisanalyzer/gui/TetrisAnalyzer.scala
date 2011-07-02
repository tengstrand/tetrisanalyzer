package nu.tengstrand.tetrisanalyzer.gui

import scala.swing._
import nu.tengstrand.tetrisanalyzer.game._
import actors.Actor._
import nu.tengstrand.tetrisanalyzer.settings.DefaultColorSettings

object TetrisAnalyzer extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "Tetris Analyzer - by Joakim Tengstrand"

    preferredSize = new Dimension(820,540)

    val label = new Label {
      text = "testing"
    }

    val colorSettings = new DefaultColorSettings
    val gameView = new GameView(colorSettings)
    val timer = new Timer(this, gameView)
    val game = new Game(timer, gameView)

    actor {
      timer.start
    }

    contents = new BoxPanel(Orientation.Horizontal) {
      contents += gameView
    }

    new KeyManager(game)
  }
}
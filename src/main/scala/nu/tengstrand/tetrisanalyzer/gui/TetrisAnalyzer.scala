package nu.tengstrand.tetrisanalyzer.gui

import scala.swing._
import nu.tengstrand.tetrisanalyzer.game._
import actors.Actor._
import nu.tengstrand.tetrisanalyzer.settings.DefaultColorSettings

object TetrisAnalyzer extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "Tetris Analyzer - by Joakim Tengstrand"

    preferredSize = new Dimension(850,560)

    val label = new Label {
      text = "testing"
    }

    val colorSettings = new DefaultColorSettings
    val gameView = new GameView(colorSettings, new MainFrameSize(this))
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

  class MainFrameSize(frame: Frame) {
    def width = frame.size.width
    def height = frame.size.height
  }
}
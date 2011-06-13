package nu.tengstrand.tetrisanalyzer.gui

import scala.swing._
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings
import nu.tengstrand.tetrisanalyzer.game._
import actors.Actor._

object TetrisAnalyzer extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "Tetris Analyzer - by Joakim Tengstrand"

    preferredSize = new Dimension(650,550)

    val label = new Label {
      text = "testing"
    }

    val settings = new DefaultGameSettings
    val positionView = new PositionView(settings)
    val gameInfoView = new GameInfoView()
    val timer = new Timer(this, gameInfoView)
    val game = new Game(settings, timer, positionView, gameInfoView)

    actor {
      timer.start
    }

    contents = new BoxPanel(Orientation.Horizontal) {
      contents += positionView
      contents += gameInfoView
    }

//    positionView.background = new Color(0,255,0)
//    gameInfoView.background = new Color(255,0,0)

    positionView.preferredSize = new Dimension(500, 550)
    positionView.size = new Dimension(500, 550)

    gameInfoView.preferredSize = new Dimension(150, 550)
    gameInfoView.size = new Dimension(150, 550)

    new KeyManager(game)
  }
}
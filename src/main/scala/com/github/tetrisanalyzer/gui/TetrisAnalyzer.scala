package com.github.tetrisanalyzer.gui

import scala.swing._
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1
import com.github.tetrisanalyzer.piecegenerator.DefaultPieceGenerator
import com.github.tetrisanalyzer.settings.DefaultGameSettings
import com.github.tetrisanalyzer.game._

object TetrisAnalyzer extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "Tetris Analyzer - by Joakim Tengstrand"

    preferredSize = new Dimension(650,550)

    val label = new Label {
      text = "testing"
    }

    val board = Board()
    val boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height)
    val pieceGenerator = new DefaultPieceGenerator(5)
    val settings = new DefaultGameSettings
    val position = Position()
    val positionView = new PositionView(settings)
    val gameInfoView = new GameInfoView()

    val gameEventReceiver = new GameEventReceiver(position, settings, positionView, gameInfoView)
    val computerPlayer = new ComputerPlayer(board, boardEvaluator, pieceGenerator, settings, gameEventReceiver)

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

    gameEventReceiver.start
    computerPlayer.start

    new KeyManager(computerPlayer, positionView, gameInfoView)
  }
}
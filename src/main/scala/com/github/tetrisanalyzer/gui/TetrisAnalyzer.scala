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
    preferredSize = new Dimension(700,500)

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

    gameEventReceiver.start
    positionView.start
    gameInfoView.start
    computerPlayer.start

    new KeyManager(computerPlayer, positionView, gameInfoView)
  }
}
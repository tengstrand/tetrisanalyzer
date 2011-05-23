package com.github.tetrisanalyzer.gui

import scala.swing._
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1
import com.github.tetrisanalyzer.piecegenerator.DefaultPieceGenerator
import com.github.tetrisanalyzer.settings.DefaultGameSettings
import scala.actors.Actor._
import com.github.tetrisanalyzer.game.Timer._
import com.github.tetrisanalyzer.game._

object TetrisAnalyzer extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "Tetris Analyzer - by Joakim Tengstrand"
    preferredSize = new Dimension(397,500)

    val label = new Label

    val board = Board()
    val boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height)
    val pieceGenerator = new DefaultPieceGenerator(4)
    val settings = new DefaultGameSettings
    val position = Position()
    val playfield = new Playfield(settings)

    val gameEventReceiver = new GameEventReceiver(position, settings, playfield)
    val computerPlayer = new ComputerPlayer(board, boardEvaluator, pieceGenerator, settings, gameEventReceiver)

    contents = new BoxPanel(Orientation.Vertical) {
      contents += playfield
      contents += label
    }

    gameEventReceiver.start()
    playfield.start
    computerPlayer.start()

  // TODO: Handle as event!
    actor {
      fiftyTimesPerSecond(() => {
        playfield.repaint
      })
    }
  }
}
package com.github.tetrisanalyzer.gui

import scala.swing._
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1
import com.github.tetrisanalyzer.piecegenerator.DefaultPieceGenerator
import com.github.tetrisanalyzer.settings.DefaultGameSettings
import scala.actors.Actor._
import com.github.tetrisanalyzer.game.Game
import com.github.tetrisanalyzer.game.Timer._

object TetrisAnalyzer extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "Tetris Analyzer"
    preferredSize = new Dimension(250,500)

    val label = new Label

    val board = Board()
    val game = createGame(board, 6)

    val playfield = new Playfield(board)

    contents = new BoxPanel(Orientation.Vertical) {
      contents += playfield
      contents += label
    }
    val player = actor {
      game.play()
    }
    actor {
      fiftyTimesPerSecond(() => {
        playfield.repaint
        label.text_=(" Pieces: " + game.moves)
      })
    }
  }

  private def createGame(board: Board, seed: Long) = {
    val boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height)
    val pieceGenerator = new DefaultPieceGenerator(seed)
    val settings = new DefaultGameSettings
    new Game(board, boardEvaluator, pieceGenerator, settings)
  }
}
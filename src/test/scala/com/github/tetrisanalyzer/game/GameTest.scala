package com.github.tetrisanalyzer.game

import com.github.tetrisanalyzer.Profiling._
import org.junit.Test
import com.github.tetrisanalyzer.BaseTest
import com.github.tetrisanalyzer.piece._
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.settings.{DefaultGameSettings, GameSettingsSlidingOn}
import com.github.tetrisanalyzer.piecegenerator.{DefaultPieceGenerator, PredictablePieceGenerator}

class GameTest extends BaseTest {

/*
  @Test def playFivePieces() {
    val board = Board(10,15)
    val boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height)
    val pieceGenerator = new PredictablePieceGenerator(List(new PieceO, new PieceL, new PieceI, new PieceZ, new PieceT))
    val settings = new GameSettingsSlidingOn
    val game = new Game(board, boardEvaluator, pieceGenerator, settings)
    game.play(5)

    game.clearedLines should be (1)

    board should be (Board(Array(
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#---------Z#",
      "#--------ZZ#",
      "#OO---TTTZL#",
      "############")))
  }
*/


  @Test def play() {
    val board = Board()
    val boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height)
    val pieceGenerator = new DefaultPieceGenerator
    val settings = new DefaultGameSettings
    val game = new Game(board, boardEvaluator, pieceGenerator, settings)

    timed(printTime("#game.play: ")){
      game.play(100000)
    }
  }


  // 10 000 = 6.976 sec
  // 20 000 = 11.2 sec
  // 100 000 = 48.614 sec = 2057 pieces/sec

  // 100 000 = 20.275 sec = 4932 pieces/sec (sliding on)
  // 100 000 = 9.529 sec = 10494 pieces/sec (sliding off)
}

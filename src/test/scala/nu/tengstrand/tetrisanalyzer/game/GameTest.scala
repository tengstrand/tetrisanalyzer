package nu.tengstrand.tetrisanalyzer.game

import org.junit.Test
import nu.tengstrand.tetrisanalyzer.piece._
import nu.tengstrand.tetrisanalyzer.boardevaluator.JTengstrandBoardEvaluator1
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.settings.GameSettingsSlidingOn
import nu.tengstrand.tetrisanalyzer.piecegenerator.PredictablePieceGenerator
import nu.tengstrand.tetrisanalyzer.BaseTest
class GameTest extends BaseTest {

  @Test def playFivePieces() {
    val board = Board(10,15)
    val boardEvaluator = new JTengstrandBoardEvaluator1(board.width, board.height)
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

/*
  @Test def play() {
    val board = Board()
    val boardEvaluator = new JTengstrandBoardEvaluator1(board.width, board.height)
    val pieceGenerator = new DefaultPieceGenerator(4)
    val settings = new DefaultGameSettings
    val game = new Game(board, boardEvaluator, pieceGenerator, settings)

    timer1.start
    game.play(1000000)
    timer1.stop

    println(board)

    println("moves: " + game.moves)
    timer1.printResult
    timer2.printResult
  }
*/
  // Performance before optimization (code at http://sourceforge.net/projects/tetrisai:
  // 1 000 000 pieces, sliding on  = 491 sec = 2 000 pieces/sec (274 sec = 56% board evaluation, 44% other)
  // 1 000 000 pieces, sliding off = 451 sec = 2 200 pieces/sec (329 sec = 72% board evaluation, 39% other)

  // Performance before optimization, with optimized JTengstrandBoardEvaluator1 (code at http://sourceforge.net/projects/tetrisai:
  // 1 000 000 pieces, sliding on  = 396 sec = 2 500 pieces/sec (220 sec = 55% board evaluation, 45% other)
  // 1 000 000 pieces, sliding off = 338 sec = 2 900 pieces/sec (217 sec = 64% board evaluation, 39% other)

  // Performance before optimizations of TetrisBoardEvaluator1 (code at https://github.com/tengstrand/tetrisanalyzer)
  // 1 000 000 pieces, sliding on  = 355 sec =  2 800 pieces/sec (132 sec = 37% board evaluation, 63% other)
  // 1 000 000 pieces, sliding off =  163 sec = 6 100 pieces/sec (128 sec = 78% board evaluation, 22% other)

  // Performance after optimizations of TetrisBoardEvaluator1 (code at https://github.com/tengstrand/tetrisanalyzer)
  // 1 000 000 pieces, sliding on  = 270 sec =  3 700 pieces/sec (44 sec = 16% board evaluation, 84% other)
  // 1 000 000 pieces, sliding off =  68 sec = 14 700 pieces/sec (35 sec = 51% board evaluation, 49% other)

  // Performance Java version (code at https://github.com/tengstrand/tetrisanalyzer))
  // 1 000 000 pieces, sliding on =  200 sec =  5 000 pieces/sec (29 sec = 14% board evaluation, 49% other)
  // 1 000 000 pieces, sliding off =  63 sec = 15 800 pieces/sec (24 sec = 38% board evaluation, 62% other)
}

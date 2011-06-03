package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.settings.GameSettings
import nu.tengstrand.tetrisanalyzer.boardevaluator.BoardEvaluator
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piecegenerator.PieceGenerator
import nu.tengstrand.tetrisanalyzer.move.{EvaluatedMoves, ValidMoves}
import nu.tengstrand.tetrisanalyzer.piecemove.{PieceMove, AllValidPieceMovesForEmptyBoard}

/**
 * Plays a game of Tetris using specified board, board evaluator and settings.
 */
class Game(board: Board, boardEvaluator: BoardEvaluator, pieceGenerator: PieceGenerator, settings: GameSettings) {
  var moves = 0L
  var clearedLines = 0L
  val allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings)
  val maxEquity = boardEvaluator.evaluate(board.worstBoard)

  /**
   * Keeps playing till the end.
   */
  def play() {
    var bestMove = evaluateBestMove

    while (bestMove.isDefined) {
      moves += 1
      clearedLines += bestMove.get.setPiece
      bestMove = evaluateBestMove
      Thread.sleep(500)
    }
  }

  /**
   * Plays the specified number of pieces, used for test.
   */
  def play(maxMoves: Long) {
    var bestMove = evaluateBestMove

    while (moves < maxMoves && bestMove.isDefined) {
      moves += 1
      clearedLines += bestMove.get.setPiece
      bestMove = evaluateBestMove
    }
  }

  private def evaluateBestMove: Option[PieceMove] = {
    val startPieceMove = allValidPieceMoves.startMoveForPiece(pieceGenerator.nextPiece)
    if (startPieceMove.isFree) {
      val validMoves = ValidMoves(board).pieceMoves(startPieceMove)
      EvaluatedMoves(board, validMoves, boardEvaluator, allValidPieceMoves.startPieces, settings.firstFreeRowUnderStartPiece, maxEquity).bestMove
    } else {
      None
    }
  }
}

package com.github.tetrisanalyzer.game

import com.github.tetrisanalyzer.settings.GameSettings
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator
import com.github.tetrisanalyzer.move.{EvaluatedMoves, ValidMoves}
import com.github.tetrisanalyzer.piecemove.{PieceMove, AllValidPieceMovesForEmptyBoard}

/**
 * Plays a game of Tetris using specified board, board evaluator and settings.
 */
class Game(board: Board, boardEvaluator: BoardEvaluator, pieceGenerator: PieceGenerator, settings: GameSettings) {
  var moves = 0L
  var clearedLines = 0L
  val allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings)

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
      EvaluatedMoves(validMoves, boardEvaluator).bestMove
    } else {
      None
    }
  }
}

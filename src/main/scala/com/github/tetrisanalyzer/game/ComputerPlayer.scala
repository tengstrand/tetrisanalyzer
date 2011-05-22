package com.github.tetrisanalyzer.game

import com.github.tetrisanalyzer.settings.GameSettings
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator
import com.github.tetrisanalyzer.move.{EvaluatedMoves, ValidMoves}
import com.github.tetrisanalyzer.piecemove.{PieceMove, AllValidPieceMovesForEmptyBoard}
import actors.Actor

/**
 * Plays a game of Tetris using specified board, board evaluator and settings.
 */
class ComputerPlayer(board: Board, boardEvaluator: BoardEvaluator, pieceGenerator: PieceGenerator,
                     settings: GameSettings, gameEventReceiver: GameEventReceiver) extends Actor {
  val allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings)

  var clearedLines = 0L

  override def act() {
    var bestMove = evaluateBestMove
    var moves = 0

    while (bestMove.isDefined) {
      clearedLines += bestMove.get.setPiece
      gameEventReceiver ! SetPiece(bestMove.get.piece, bestMove.get.move)
      bestMove = evaluateBestMove
      moves += 1
      Thread.sleep(500) // Send two pieces per second
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

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

  private var paused = true
  private var doStep = true

  def setPaused(paused: Boolean) {
    doStep = true
    this.paused = paused
  }
  def performStep() { doStep = true }

  override def act() {
    var bestMove = evaluateBestMove

    while (bestMove.isDefined) {
      while (paused && !doStep)
          Thread.sleep(20)
      bestMove = makeMove(bestMove.get)
    }
  }

  private def makeMove(pieceMove: PieceMove): Option[PieceMove] = {
    doStep = false
    val clearedLines: Long = pieceMove.setPiece
    gameEventReceiver ! SetPieceMessage(pieceMove.piece, pieceMove.move, clearedLines)
    evaluateBestMove
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

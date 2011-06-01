package com.github.tetrisanalyzer.game

import com.github.tetrisanalyzer.settings.GameSettings
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator
import com.github.tetrisanalyzer.move.{EvaluatedMoves, ValidMoves}
import com.github.tetrisanalyzer.piecemove.{PieceMove, AllValidPieceMovesForEmptyBoard}
import actors.Actor
import com.github.tetrisanalyzer.piece.Piece

/**
 * Plays a game of Tetris using specified board, board evaluator and settings.
 */
class ComputerPlayer(board: Board, position: Position, boardEvaluator: BoardEvaluator, pieceGenerator: PieceGenerator,
                     settings: GameSettings, playerEventReceiver: PlayerEventReceiver, gameInfoReceiver: GameInfoReceiver) extends Actor {
  val allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings)

  private var paused = true
  private var doStep = true

  private var moves = 0L
  private var totalClearedLines = 0L

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
      moves += 1
    }

    updatePlayerReceiver

    println(board.toString)
  }

  private def makeMove(pieceMove: PieceMove): Option[PieceMove] = {
    val clearedLines: Long = pieceMove.setPiece

    // Update GUI every 100 piece.
    if (doStep || moves % 100 == 0) {
      updatePlayerReceiver(pieceMove.piece)
      updateGameInfoReceiver()
    }
    doStep = false

    position.setPiece(pieceMove.piece, pieceMove.move)
    if (clearedLines > 0)
      position.clearLines(pieceMove.move.y, pieceMove.piece.height(pieceMove.move.rotation))

    totalClearedLines += clearedLines

    evaluateBestMove
  }

  private def updatePlayerReceiver() {
    playerEventReceiver.setPosition(Position(position))
  }

  private def updatePlayerReceiver(piece: Piece) {
    val positionWithStartPiece = Position(position)
    positionWithStartPiece.setStartPieceIfFree(piece, settings)
    playerEventReceiver.setPosition(positionWithStartPiece)
  }

  private def updateGameInfoReceiver() {
    gameInfoReceiver.setPieces(moves)
    gameInfoReceiver.setTotalClearedLines(totalClearedLines)
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

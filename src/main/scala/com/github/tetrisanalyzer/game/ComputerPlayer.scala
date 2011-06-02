package com.github.tetrisanalyzer.game

import com.github.tetrisanalyzer.settings.GameSettings
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator
import com.github.tetrisanalyzer.piecemove.{PieceMove, AllValidPieceMovesForEmptyBoard}
import actors.Actor
import com.github.tetrisanalyzer.piece.Piece
import com.github.tetrisanalyzer.move.{Move, EvaluatedMoves, ValidMoves}

/**
 * Plays a game of Tetris using specified board, board evaluator and settings.
 */
class ComputerPlayer(board: Board, startPosition: Position, boardEvaluator: BoardEvaluator, pieceGenerator: PieceGenerator,
                     settings: GameSettings, gameEventReceiver: GameEventReceiver) extends Actor {
  val allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings)

  private val startBoard = board.copy
  private var position: Position = null
  private var paused = true
  private var doStep = true

  private var moves = 0L
  private var movesTotal = 0L
  private var clearedLines = 0L
  private var clearedLinesTotal = 0L
  private var games = 0

  def setPaused(paused: Boolean) {
    doStep = true
    this.paused = paused
  }
  def performStep() { doStep = true }

  override def act() {
    while (true) {
      board.restore(startBoard)
      position = Position(startPosition)
      var bestMove = evaluateBestMove

      while (bestMove.isDefined) {
        waitIfPaused
        bestMove = makeMove(bestMove.get)
        moves += 1
        movesTotal += 1
      }

      games += 1
      updateEndPositionInGUI
      moves = 0
      clearedLines = 0
    }
  }

  private def waitIfPaused() {
    while (paused && !doStep)
        Thread.sleep(20)
  }

  private def makeMove(pieceMove: PieceMove): Option[PieceMove] = {
    val clearedLines: Long = pieceMove.setPiece

    // Update GUI every 100 piece.
    if (doStep || moves % 100 == 0) {
      updatePositionInGUI(pieceMove.piece)
      updateGameInfoInGUI()
    }
    setPieceOnPosition(pieceMove.piece, pieceMove.move, clearedLines)

    doStep = false
    this.clearedLines += clearedLines
    clearedLinesTotal += clearedLines

    evaluateBestMove
  }

  private def setPieceOnPosition(piece: Piece, move: Move, clearedLines: Long) {
    position.setPiece(piece, move)
    if (clearedLines > 0)
      position.clearLines(move.y, piece.height(move.rotation))
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

  private def updateEndPositionInGUI() {
    gameEventReceiver.setPosition(Position(position))
    gameEventReceiver.setNumberOfGamesAndLinesInLastGame(games, clearedLines)
  }

  private def updatePositionInGUI(piece: Piece) {
    val positionWithStartPiece = Position(position)
    positionWithStartPiece.setStartPieceIfFree(piece, settings)
    gameEventReceiver.setPosition(positionWithStartPiece)
  }

  private def updateGameInfoInGUI() {
    gameEventReceiver.setNumberOfPieces(moves)
    gameEventReceiver.setTotalNumberOfPieces(movesTotal)
    gameEventReceiver.setNumberOfClearedLines(clearedLines)
    gameEventReceiver.setTotalNumberOfClearedLines(clearedLinesTotal)
    gameEventReceiver.updateGui()
  }
}

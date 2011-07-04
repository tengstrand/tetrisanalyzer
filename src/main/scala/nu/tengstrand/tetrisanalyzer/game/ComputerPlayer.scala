package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.settings.GameSettings
import nu.tengstrand.tetrisanalyzer.boardevaluator.BoardEvaluator
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piecegenerator.PieceGenerator
import nu.tengstrand.tetrisanalyzer.piecemove.{PieceMove, AllValidPieceMovesForEmptyBoard}
import actors.Actor
import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.move.{Move, EvaluatedMoves, ValidMoves}
import java.awt.Dimension

/**
 * Plays a game of Tetris using specified board, board evaluator and settings.
 */
class ComputerPlayer(speed: Speed, board: Board, startPosition: Position, boardEvaluator: BoardEvaluator, pieceGenerator: PieceGenerator,
                     settings: GameSettings, gameEventReceiver: GameEventReceiver) extends Actor {

  private val maxEquity = boardEvaluator.evaluate(board.junkBoard)
  private val allValidPieceMovesForEmptyBoard = new AllValidPieceMovesForEmptyBoard(board, settings)

  private val startBoard = board.copy
  private var position: Position = null
  private var paused = Game.PausedOnStartup
  private var doStep = false
  private var quit = false
  private val gameStatistics = new GameStatistics(new Dimension(board.width, board.height), settings.pieceGeneratorSeed, gameEventReceiver)
  private val pieceMoveAnimator = new PieceMoveAnimator(speed, gameEventReceiver)

  def setPaused(paused: Boolean) {
    doStep = pieceMoveAnimator.continueDoStep(paused)
    this.paused = paused
  }

  def increaseSpeed() {
    pieceMoveAnimator.increaseSpeed()
    updateSpeed()
  }

  def decreaseSpeed() {
    pieceMoveAnimator.decreaseSpeed()
    updateSpeed()
  }

  private def updateSpeed() {
    gameEventReceiver.setSpeed(pieceMoveAnimator.speedAsName)
    doStep = pieceMoveAnimator.continueDoStep(paused)
  }

  def performStep() {
    pieceMoveAnimator.fastAnimation = doStep && paused || (!paused && !pieceMoveAnimator.isMaxSpeed)
    doStep = true
  }

  def quitGame() { quit = true; pieceMoveAnimator.quit = true; Thread.sleep(pieceMoveAnimator.maxDelay) }

  override def act() {
    gameStatistics.updateAll()
    gameEventReceiver.setSliding(settings.isSlidingEnabled)
    gameEventReceiver.setTimePassed(0)
    gameEventReceiver.setSpeed(pieceMoveAnimator.speedAsName)

    while (!quit) {
      board.restore(startBoard)
      position = Position(startPosition)
      var startPieceMove = nextPiece
      var bestMove = evaluateBestMove(startPieceMove)

      while (!quit && bestMove.isDefined) {
        waitIfPaused(startPieceMove.piece)
        if (doStep)
          pieceMoveAnimator.animateMove(position, startPieceMove, bestMove.get)

        startPieceMove = nextPiece
        bestMove = makeMove(startPieceMove, bestMove.get)
        gameStatistics.addMove()
      }
      if (!quit) {
        gameStatistics.newGame()
      }
    }
    exit()
  }

  private def nextPiece = allValidPieceMovesForEmptyBoard.startMoveForPiece(pieceGenerator.nextPiece)

  private def waitIfPaused(startPiece: Piece) {
    if (paused && !quit) {
      gameStatistics.updatePosition(position, startPiece, settings)
      gameStatistics.updateGameInfo
    }

    while (paused && !doStep && !quit)
      Thread.sleep(20)
  }

  private def shouldGameInfoBeUpdated = doStep || gameStatistics.hasPassedHundredPieces

  private def shouldRankedMovesBeUpdated = paused || shouldGameInfoBeUpdated

  private def makeMove(startPieceMove: PieceMove, pieceMove: PieceMove): Option[PieceMove] = {
    val clearedRows = pieceMove.setPiece

    // Update GUI every 100 piece and always if in step mode
    if (shouldGameInfoBeUpdated) {
      if (!doStep)
        gameStatistics.updatePosition(position, pieceMove.piece, settings)
      gameStatistics.updateGameInfo()
    }
    setPieceOnPosition(pieceMove.piece, pieceMove.move, clearedRows)

    doStep = pieceMoveAnimator.continueDoStep(paused)
    pieceMoveAnimator.fastAnimation = false
    gameStatistics.addClearedRows(clearedRows)

    evaluateBestMove(startPieceMove)
  }

  private def setPieceOnPosition(piece: Piece, move: Move, clearedRows: Long) {
    position.setPiece(piece, move)
    if (clearedRows > 0) {
      val pieceHeight = piece.height(move.rotation)
      if (doStep)
        pieceMoveAnimator.animateClearedRows(position, move.y, pieceHeight, gameEventReceiver)
      position.clearRows(move.y, pieceHeight)
    }
  }

  private def evaluateBestMove(startPieceMove: PieceMove): Option[PieceMove] = {
    if (startPieceMove.isFree) {
      val validMoves = ValidMoves(board).pieceMoves(startPieceMove)
      val evaluatedMoves = EvaluatedMoves(board, validMoves, boardEvaluator, allValidPieceMovesForEmptyBoard.startPieces, settings.firstFreeRowUnderStartPiece, maxEquity)

      if (shouldRankedMovesBeUpdated) {
        gameEventReceiver.setRankedMoves(evaluatedMoves.sortedMovesWithAdjustedEquity)
      }

      evaluatedMoves.bestMove
    } else {
      None
    }
  }
}

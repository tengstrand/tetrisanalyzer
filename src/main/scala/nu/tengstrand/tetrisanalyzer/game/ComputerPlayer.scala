package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.settings.GameSettings
import nu.tengstrand.tetrisanalyzer.boardevaluator.BoardEvaluator
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piecemove.{PieceMove, AllValidPieceMovesForEmptyBoard}
import actors.Actor
import nu.tengstrand.tetrisanalyzer.piece.Piece
import java.awt.Dimension
import nu.tengstrand.tetrisanalyzer.gui.rankedmove.RankedMoves
import startpiece.{StartPiece, StartPieceGenerator}
import nu.tengstrand.tetrisanalyzer.move.{Move, EvaluatedMoves, ValidMoves}
import nu.tengstrand.tetrisanalyzer.piecegenerator.PieceGenerator

/**
 * Plays a game of Tetris using specified board, board evaluator and settings.
 */
class ComputerPlayer(speed: Speed, startPieceGenerator: StartPieceGenerator, board: Board, position: Position, boardEvaluator: BoardEvaluator,
                     settings: GameSettings, var slidingEnabled: Boolean, gameEventReceiver: GameEventReceiver) extends Actor {

  private val maxEquity = boardEvaluator.evaluate(board.junkBoard)

  private var startPiece: StartPiece = null
  private var startPieceMove: PieceMove = null
  private var nextPieceMove: PieceMove = null

  private var allValidPieceMovesForEmptyBoard = new AllValidPieceMovesForEmptyBoard(board, settings, slidingEnabled)

  private val startBoard = board.copy
  private var startPosition = Position(position)
  private var paused = Game.PausedAtStartup
  private var doStep = false
  private var quit = false
  private var showNextPiece = false

  private val gameStatistics = new GameStatistics(new Dimension(board.width, board.height), gameEventReceiver)
  private val pieceMoveAnimator = new PieceMoveAnimator(speed, gameEventReceiver)

  private var showRankedMoves = false
  private var rankedMoves: RankedMoves = new RankedMoves(board.width, board.height)

  private var bestMove: Option[PieceMove] = null

  /*
   * This is where the computer plays.
   */
  override def act() {
    gameStatistics.updateAll()
    gameEventReceiver.setSliding(slidingEnabled)
    gameEventReceiver.setTimePassed(0)
    gameEventReceiver.setSpeed(pieceMoveAnimator.getSpeedIndex, pieceMoveAnimator.isMaxSpeed)

    while (!quit) {
      board.restore(startBoard)
      position.copyFrom(startPosition)

      initCurrentAndNextStartPiece(startPieceGenerator.piece(showNextPiece))
      evaluateBestMove()

      while (!quit && bestMove.isDefined) {
        position.setOrRemoveNextPiece(startPiece)
        waitIfPaused()

        if (!quit) {
          val bestOrSelectedMove = bestOrSelectedPieceMove(bestMove)
          if (doStep)
            pieceMoveAnimator.animateMove(position, startPiece, startPieceMove, bestOrSelectedMove)

          initCurrentAndNextStartPiece(startPieceGenerator.nextPiece(showNextPiece))

          makeMove(bestOrSelectedMove)
          evaluateBestMove()

          gameStatistics.addMove()
        }
      }
      if (!quit)
        gameStatistics.newGame()
    }
    exit()
  }

  def setShowRankedMoves(show: Boolean) {
    showRankedMoves = show
    evaluateBestMove(true)
    updateSpeed()
  }

  def setPaused(paused: Boolean) {
    doStep = pieceMoveAnimator.continueDoStep(paused)
    this.paused = paused
  }

  def setPieceGenerator(pieceGenerator: PieceGenerator) {
    startPieceGenerator.setPieceGenerator(pieceGenerator)
    initCurrentAndNextStartPiece(startPieceGenerator.piece(showNextPiece))
    evaluateBestMove(false)
    position.setOrRemoveNextPiece(startPiece)
    resetGameInfo()
  }

  def setSliding(slidingEnabled: Boolean) {
    this.slidingEnabled = slidingEnabled
    allValidPieceMovesForEmptyBoard = new AllValidPieceMovesForEmptyBoard(board, settings, slidingEnabled)
    initCurrentAndNextStartPiece(startPieceGenerator.piece(showNextPiece))
    evaluateBestMove(true)
    gameStatistics.reset()
    resetGameInfo()
  }

  def setShowNextPiece(show: Boolean) {
    this.showNextPiece = show
    initCurrentAndNextStartPiece(startPieceGenerator.piece(show))
    position.setOrRemoveNextPiece(startPiece)
    evaluateBestMove(true)
    resetGameInfo()
  }

  def resetGameInfo() {
    updateSpeed()
    gameStatistics.reset()
  }

  def increaseSpeed() {
    pieceMoveAnimator.increaseSpeed()
    updateSpeed()
  }

  def decreaseSpeed() {
    pieceMoveAnimator.decreaseSpeed()
    updateSpeed()
  }

  def toggleMaxSpeed() {
    pieceMoveAnimator.toggleMaxSpeed()
    updateSpeed()
  }

  private def updateSpeed() {
    gameStatistics.updateGameInfo()
    gameEventReceiver.setSpeed(pieceMoveAnimator.getSpeedIndex, pieceMoveAnimator.isMaxSpeed)
    doStep = pieceMoveAnimator.continueDoStep(paused)
  }

  def performStep() {
    pieceMoveAnimator.fastAnimation = doStep && paused || (!paused && !pieceMoveAnimator.isMaxSpeed)
    doStep = true
  }

  def quitGame() { quit = true; pieceMoveAnimator.quit = true; waitForComputerPlayerToFinish() }

  private def waitForComputerPlayerToFinish() { Thread.sleep(Speed.MaxDelay) }

  private def initCurrentAndNextStartPiece(startPiece: StartPiece) {
    this.startPiece = startPiece
    startPieceMove = allValidPieceMovesForEmptyBoard.startMoveForPiece(startPiece.firstPiece)
    nextPieceMove = if (startPiece.hasNext) allValidPieceMovesForEmptyBoard.startMoveForPiece(startPiece.secondPiece) else null
  }

  private def bestOrSelectedPieceMove(bestPieceMove: Option[PieceMove]) = {
    if (hasRankedMoves)
      rankedMoves.selectedPieceMove
    else
      bestPieceMove.get
  }

  private def selectedRankedMove = {
    if (hasRankedMoves)
      rankedMoves.selectedPieceMove.move
    else
      null
  }

  private def hasRankedMoves = showRankedMoves && !rankedMoves.isEmpty

  private def waitIfPaused() {
    if (paused && !quit)
      updatePosition(startPiece)

    while (paused && !doStep && !quit) {
      updatePosition(startPiece)
      Thread.sleep(20)
    }
  }

  private def updatePosition(startPiece: StartPiece) {
    gameStatistics.setStartPieceAndSelectedMove(position, startPiece, selectedRankedMove, settings)
    gameStatistics.updateGameInfo()
  }

  private def shouldGameInfoBeUpdated = doStep || gameStatistics.isTimeToUpdateGui(showNextPiece)

  private def makeMove(pieceMove: PieceMove) {
    val clearedRows = pieceMove.setPiece()

    // Update GUI every 10th or 100th piece move, and always if paused
    if (shouldGameInfoBeUpdated) {
      if (!doStep)
        gameStatistics.setStartPieceAndSelectedMove(position, startPiece, selectedRankedMove, settings)
      gameStatistics.updateGameInfo()
    }
    setPieceOnPosition(pieceMove.piece, pieceMove.move, clearedRows)

    doStep = pieceMoveAnimator.continueDoStep(paused)
    pieceMoveAnimator.fastAnimation = false
    gameStatistics.addClearedRows(clearedRows)
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

  private def evaluateBestMove(keepSelectedMove: Boolean = false) {
    if (startPieceMove.isFree) {
      val validMoves = ValidMoves(board).pieceMoves(startPieceMove)
      val evaluatedMoves = EvaluatedMoves(board, validMoves, boardEvaluator, startPiece, nextPieceMove, allValidPieceMovesForEmptyBoard.startPieces, settings.firstGuaranteedFreeRowUnderStartPiece, maxEquity)

      if (showRankedMoves || paused)
        notifySelectedRankedMove(startPieceMove, evaluatedMoves, keepSelectedMove)

      bestMove = evaluatedMoves.bestMove
    } else {
      bestMove = None
    }
  }

  private def notifySelectedRankedMove(startPieceMove: PieceMove, evaluatedMoves: EvaluatedMoves, keepSelectedMove: Boolean) {
    val sortedMoves = evaluatedMoves.sortedMovesWithAdjustedEquity
    rankedMoves.setMoves(sortedMoves, keepSelectedMove)

    gameEventReceiver.setRankedMoves(rankedMoves)
  }
}

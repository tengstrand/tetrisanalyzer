package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.settings.GameSettings
import nu.tengstrand.tetrisanalyzer.boardevaluator.BoardEvaluator
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piecemove.{PieceMove, AllValidPieceMovesForEmptyBoard}
// Actor replaced with Thread for Scala 3 compatibility
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
                     settings: GameSettings, var slidingEnabled: Boolean, gameEventReceiver: GameEventReceiver) extends Thread {

  private val maxEquity = boardEvaluator.evaluate(board.junkBoard)

  private var startPiece: Option[StartPiece] = None
  private var startPieceMove: Option[PieceMove] = None
  private var nextPieceMove: Option[PieceMove] = None

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

  private var bestMove: Option[PieceMove] = None

  /*
   * This is where the computer plays.
   */
  override def run(): Unit = {
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
        startPiece.foreach(position.setOrRemoveNextPiece)
        waitIfPaused()

        if (!quit) {
          val bestOrSelectedMove = bestOrSelectedPieceMove(bestMove)
          if (doStep)
            (startPiece, startPieceMove) match {
              case (Some(sp), Some(spm)) => pieceMoveAnimator.animateMove(position, sp, spm, bestOrSelectedMove)
              case _ => // Skip animation if pieces not available
            }

          initCurrentAndNextStartPiece(startPieceGenerator.nextPiece(showNextPiece))

          makeMove(bestOrSelectedMove)
          evaluateBestMove()

          gameStatistics.addMove()
        }
      }
      if (!quit)
        gameStatistics.newGame()
    }
  }

  def setShowRankedMoves(show: Boolean): Unit = {
    showRankedMoves = show
    evaluateBestMove(true)
    updateSpeed()
  }

  def setPaused(paused: Boolean): Unit = {
    doStep = pieceMoveAnimator.continueDoStep(paused)
    this.paused = paused
  }

  def setPieceGenerator(pieceGenerator: PieceGenerator): Unit = {
    startPieceGenerator.setPieceGenerator(pieceGenerator)
    initCurrentAndNextStartPiece(startPieceGenerator.piece(showNextPiece))
    evaluateBestMove(false)
    startPiece.foreach(position.setOrRemoveNextPiece)
    resetGameInfo()
  }

  def setSliding(slidingEnabled: Boolean): Unit = {
    this.slidingEnabled = slidingEnabled
    allValidPieceMovesForEmptyBoard = new AllValidPieceMovesForEmptyBoard(board, settings, slidingEnabled)
    initCurrentAndNextStartPiece(startPieceGenerator.piece(showNextPiece))
    evaluateBestMove(true)
    gameStatistics.reset()
    resetGameInfo()
  }

  def setShowNextPiece(show: Boolean): Unit = {
    this.showNextPiece = show
    initCurrentAndNextStartPiece(startPieceGenerator.piece(show))
    startPiece.foreach(position.setOrRemoveNextPiece)
    evaluateBestMove(true)
    resetGameInfo()
  }

  def resetGameInfo(): Unit = {
    gameStatistics.reset()
    updateSpeed()
  }

  def increaseSpeed(): Unit = {
    pieceMoveAnimator.increaseSpeed()
    updateSpeed()
  }

  def decreaseSpeed(): Unit = {
    pieceMoveAnimator.decreaseSpeed()
    updateSpeed()
  }

  def toggleMaxSpeed(): Unit = {
    pieceMoveAnimator.toggleMaxSpeed()
    updateSpeed()
  }

  private def updateSpeed(): Unit = {
    gameStatistics.updateGameInfo()
    gameEventReceiver.setSpeed(pieceMoveAnimator.getSpeedIndex, pieceMoveAnimator.isMaxSpeed)
    doStep = pieceMoveAnimator.continueDoStep(paused)
  }

  def performStep(): Unit = {
    pieceMoveAnimator.fastAnimation = doStep && paused || (!paused && !pieceMoveAnimator.isMaxSpeed)
    doStep = true
  }

  def quitGame(): Unit = { quit = true; pieceMoveAnimator.quit = true; waitForComputerPlayerToFinish() }

  private def waitForComputerPlayerToFinish(): Unit = { Thread.sleep(Speed.MaxDelay) }

  private def initCurrentAndNextStartPiece(startPiece: StartPiece): Unit = {
    this.startPiece = Some(startPiece)
    startPieceMove = Some(allValidPieceMovesForEmptyBoard.startMoveForPiece(startPiece.firstPiece))
    nextPieceMove = startPiece.secondPiece.map(allValidPieceMovesForEmptyBoard.startMoveForPiece)
  }

  private def bestOrSelectedPieceMove(bestPieceMove: Option[PieceMove]) = {
    if (hasRankedMoves)
      rankedMoves.selectedPieceMove
    else
      bestPieceMove.get
  }

  private def selectedRankedMove: Option[Move] = {
    if (hasRankedMoves)
      Some(rankedMoves.selectedPieceMove.move)
    else
      None
  }

  private def hasRankedMoves = showRankedMoves && !rankedMoves.isEmpty

  private def waitIfPaused(): Unit = {
    if (paused && !quit)
      startPiece.foreach(updatePosition)

    while (paused && !doStep && !quit) {
      startPiece.foreach(updatePosition)
      Thread.sleep(20)
    }
  }

  private def updatePosition(startPiece: StartPiece): Unit = {
    gameStatistics.setStartPieceAndSelectedMove(position, startPiece, selectedRankedMove, settings)
    gameStatistics.updateGameInfo()
  }

  private def shouldGameInfoBeUpdated = doStep || gameStatistics.isTimeToUpdateGui(showNextPiece)

  private def makeMove(pieceMove: PieceMove): Unit = {
    val clearedRows = pieceMove.setPiece()

    // Update GUI every 10th or 100th piece move, and always if paused
    if (shouldGameInfoBeUpdated) {
      if (!doStep)
        startPiece.foreach(sp => gameStatistics.setStartPieceAndSelectedMove(position, sp, selectedRankedMove, settings))
      gameStatistics.updateGameInfo()
    }
    setPieceOnPosition(pieceMove.piece, pieceMove.move, clearedRows)

    doStep = pieceMoveAnimator.continueDoStep(paused)
    pieceMoveAnimator.fastAnimation = false
    gameStatistics.addClearedRows(clearedRows)
  }

  private def setPieceOnPosition(piece: Piece, move: Move, clearedRows: Long): Unit = {
    position.setPiece(piece, move)
    if (clearedRows > 0) {
      val pieceHeight = piece.height(move.rotation)
      if (doStep)
        pieceMoveAnimator.animateClearedRows(position, move.y, pieceHeight, gameEventReceiver)
      position.clearRows(move.y, pieceHeight)
    }
  }

  private def evaluateBestMove(keepSelectedMove: Boolean = false): Unit = {
    (startPiece, startPieceMove) match {
      case (Some(sp), Some(spm)) if spm.isFree =>
        val validMoves = ValidMoves(board).pieceMoves(spm)
        val evaluatedMoves = EvaluatedMoves(board, validMoves, boardEvaluator, sp, nextPieceMove, allValidPieceMovesForEmptyBoard.startPieces, settings.firstGuaranteedFreeRowUnderStartPiece, maxEquity)

        if (showRankedMoves || paused)
          notifySelectedRankedMove(spm, evaluatedMoves, keepSelectedMove)

        bestMove = evaluatedMoves.bestMove
      case _ =>
        bestMove = None
    }
  }

  private def notifySelectedRankedMove(startPieceMove: PieceMove, evaluatedMoves: EvaluatedMoves, keepSelectedMove: Boolean): Unit = {
    val sortedMoves = evaluatedMoves.sortedMovesWithAdjustedEquity
    rankedMoves.setMoves(sortedMoves, keepSelectedMove)

    gameEventReceiver.setRankedMoves(rankedMoves)
  }
}

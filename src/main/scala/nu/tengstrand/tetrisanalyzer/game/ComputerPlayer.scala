package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.settings.GameSettings
import nu.tengstrand.tetrisanalyzer.boardevaluator.BoardEvaluator
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piecemove.{PieceMove, AllValidPieceMovesForEmptyBoard}
import actors.Actor
import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.move.{Move, EvaluatedMoves, ValidMoves}
import java.awt.Dimension
import nu.tengstrand.tetrisanalyzer.gui.rankedmove.RankedMoves
import startpiece.{StartPiece, StartPieceGenerator}

/**
 * Plays a game of Tetris using specified board, board evaluator and settings.
 */
class ComputerPlayer(speed: Speed, startPieceGenerator: StartPieceGenerator, board: Board, position: Position, boardEvaluator: BoardEvaluator,
                     settings: GameSettings, rankedMoveToSelect: Option[Move], gameEventReceiver: GameEventReceiver) extends Actor {

  private val maxEquity = boardEvaluator.evaluate(board.junkBoard)

  private var startPiece: StartPiece = null
  private var startPieceMove: PieceMove = null
  private var nextPieceMove: PieceMove = null

  private val allValidPieceMovesForEmptyBoard = new AllValidPieceMovesForEmptyBoard(board, settings)

  private val startBoard = board.copy
  private var startPosition = Position(position)
  private var paused = Game.PausedOnStartup
  private var doStep = false
  private var quit = false
  private var showNextPiece = false

  private val gameStatistics = new GameStatistics(new Dimension(board.width, board.height), gameEventReceiver)
  private val pieceMoveAnimator = new PieceMoveAnimator(speed, gameEventReceiver)

  private var showRankedMoves = false
  private var rankedMoves: RankedMoves = null

  def setShowRankedMoves(show: Boolean) {
    showRankedMoves = show
    updateSpeed()
  }

  def setPaused(paused: Boolean) {
    doStep = pieceMoveAnimator.continueDoStep(paused)
    this.paused = paused
  }

  def setShowNextPiece(show: Boolean) {
    this.showNextPiece = show
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

  /*
   * This is where the computer plays.
   */
  override def act() {
    gameStatistics.updateAll()
    gameEventReceiver.setSliding(settings.isSlidingEnabled)
    gameEventReceiver.setTimePassed(0)
    gameEventReceiver.setSpeed(pieceMoveAnimator.getSpeedIndex, pieceMoveAnimator.isMaxSpeed)

    while (!quit) {
      board.restore(startBoard)
      position.copyFrom(startPosition)

      initCurrentAndNextStartPiece(startPieceGenerator.piece(showNextPiece))
      var bestMove = evaluateBestMove()

      while (!quit && bestMove.isDefined) {
        waitIfPaused(startPiece)

        if (!quit) {
          var selectedMove = selectedPieceMove(bestMove)
          if (doStep)
            pieceMoveAnimator.animateMove(position, startPiece, startPieceMove, selectedMove)

          initCurrentAndNextStartPiece(startPieceGenerator.nextPiece(showNextPiece))

          makeMove(selectedMove)
          bestMove = evaluateBestMove()

          gameStatistics.addMove()
        }
      }
      if (!quit)
        gameStatistics.newGame()
    }
    exit()
  }

  private def initCurrentAndNextStartPiece(startPiece: StartPiece) {
    this.startPiece = startPiece
    startPieceMove = allValidPieceMovesForEmptyBoard.startMoveForPiece(startPiece.firstPiece)
    nextPieceMove = if (startPiece.hasNext) allValidPieceMovesForEmptyBoard.startMoveForPiece(startPiece.secondPiece) else null
  }

  private def selectedPieceMove(bestPieceMove: Option[PieceMove]) = {
    if (hasRankedMoves)
      rankedMoves.selectedMove.get.moveEquity.pieceMove
    else
      bestPieceMove.get
  }

  private def selectedRankedMove = {
    if (hasRankedMoves)
      rankedMoves.selectedMove.get.moveEquity.pieceMove.move
    else
      null
  }

  private def hasRankedMoves = showRankedMoves && rankedMoves != null && rankedMoves.selectedMove.isDefined

  private def waitIfPaused(startPiece: StartPiece) {
    if (paused && !quit)
      updatePosition(startPiece)

    while (paused && !doStep && !quit) {
      updatePosition(startPiece)
      Thread.sleep(20)
    }
  }

  private def updatePosition(startPiece: StartPiece) {
    gameStatistics.setStartPieceAndSelectedMoveIfSelectedOnPosition(position, startPiece, selectedRankedMove, settings)
    gameStatistics.updateGameInfo()
  }

  private def shouldGameInfoBeUpdated = doStep || gameStatistics.hasPassedHundredPieces

  private def makeMove(pieceMove: PieceMove) {
    val clearedRows = pieceMove.setPiece()

    // Update GUI every 100 piece and always if in step mode
    if (shouldGameInfoBeUpdated) {
      if (!doStep)
        gameStatistics.setStartPieceAndSelectedMoveIfSelectedOnPosition(position, startPiece, selectedRankedMove, settings)
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

  /*
  private def evaluateBestMove(startPieceMove: PieceMove, startPiece: StartPiece): Option[PieceMove] = {
    if (!startPiece.hasNext) {
      evaluateBestMove(startPieceMove)
    } else {
      val nextStartPieceMove = allValidPieceMovesForEmptyBoard.startMoveForPiece(startPiece.secondPiece)
      evaluateBestMove(startPieceMove, nextStartPieceMove)
    }
  }
  */
  /*
  private def evaluateBestMove(startPieceMove: PieceMove, nextStartPieceMove: PieceMove): Option[PieceMove] = {
    if (startPieceMove.isFree) {
      val boardCopy = board.copy
      val validMoves = ValidMoves(board).pieceMoves(startPieceMove)

      var bestEquity = Double.MaxValue
      var bestPieceMove: PieceMove = null
      validMoves.foreach(pieceMove => {
        val clearedRows = pieceMove.setPiece()
        val equity = evaluateNextPiece(nextStartPieceMove, startPiece)
        if (equity < bestEquity) {
          bestEquity = equity
          bestPieceMove = pieceMove
        }
        if (clearedRows == 0)
          pieceMove.clearPiece()
        else
          pieceMove.board.restore(boardCopy)
      })
      Some(bestPieceMove)
    } else {
      None
    }
  }
 */

 /*
  private def evaluateNextPiece(nextStartPieceMove: PieceMove, startPiece: StartPiece): Double = {
    if (nextStartPieceMove.isFree) {
      val validNextMoves = ValidMoves(board).pieceMoves(nextStartPieceMove)
      val evaluatedMoves = EvaluatedMoves(board, validNextMoves, boardEvaluator, startPiece, allValidPieceMovesForEmptyBoard.startPieces, settings.firstGuaranteedFreeRowUnderStartPiece, maxEquity)
      evaluatedMoves.bestMoveEquity.get.equity
    } else {
      maxEquity
    }
  }
 */
  private def evaluateBestMove(): Option[PieceMove] = {
    if (startPieceMove.isFree) {
      val validMoves = ValidMoves(board).pieceMoves(startPieceMove)
      val evaluatedMoves = EvaluatedMoves(board, validMoves, boardEvaluator, startPiece, nextPieceMove, allValidPieceMovesForEmptyBoard.startPieces, settings.firstGuaranteedFreeRowUnderStartPiece, maxEquity)

      if (showRankedMoves || paused)
        notifySelectedRankedMove(startPieceMove, evaluatedMoves)

      evaluatedMoves.bestMove
    } else {
      None
    }
  }

  private def notifySelectedRankedMove(startPieceMove: PieceMove, evaluatedMoves: EvaluatedMoves) {
    val board = startPieceMove.board
    val selectRow = rankedMoves == null && rankedMoveToSelect.isDefined
    val sortedMoves = evaluatedMoves.sortedMovesWithAdjustedEquity
    rankedMoves = new RankedMoves(sortedMoves, board.width, board.height)
    if (selectRow)
      rankedMoves.selectMove(rankedMoveToSelect.get)

    gameEventReceiver.setRankedMoves(rankedMoves)
  }
}

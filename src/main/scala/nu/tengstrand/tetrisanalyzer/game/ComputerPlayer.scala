package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.settings.GameSettings
import nu.tengstrand.tetrisanalyzer.boardevaluator.BoardEvaluator
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piecegenerator.PieceGenerator
import nu.tengstrand.tetrisanalyzer.piecemove.{PieceMove, AllValidPieceMovesForEmptyBoard}
import actors.Actor
import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.move.{Move, EvaluatedMoves, ValidMoves}

object MoveStep {
  val DefaultStepTime = 20
  val FastStepTime = 5
}

/**
 * Plays a game of Tetris using specified board, board evaluator and settings.
 */
class ComputerPlayer(isPaused: Boolean, board: Board, startPosition: Position, boardEvaluator: BoardEvaluator, pieceGenerator: PieceGenerator,
                     settings: GameSettings, gameEventReceiver: GameEventReceiver) extends Actor {

  private val maxEquity = boardEvaluator.evaluate(board.worstBoard)
  private val allValidPieceMovesForEmptyBoard = new AllValidPieceMovesForEmptyBoard(board, settings)

  private val startBoard = board.copy
  private var position: Position = null
  private var paused = isPaused
  private var doStep = false
  private var quit = false
  private var timeToShowStepMilisec = MoveStep.DefaultStepTime

  private var moves = 0L
  private var movesTotal = 0L
  private var clearedLines = 0L
  private var clearedLinesTotal = 0L
  private var games = 0L
  private var totalClearedLines = 0L
  private var minClearedLines = 0L
  private var maxClearedLines = 0L

  def setPaused(paused: Boolean) {
    doStep = false
    this.paused = paused
  }
  def performStep() {
    if (doStep)
      timeToShowStepMilisec = 5

    doStep = true
  }

  def quitGame() { quit = true; Thread.sleep(25) }

  override def act() {
    gameEventReceiver.setSeed(settings.pieceGeneratorSeed)
    gameEventReceiver.setBoardSize(board.width, board.height)
    gameEventReceiver.setNumberOfGamesAndLinesInLastGame(games, clearedLines, totalClearedLines, minClearedLines, maxClearedLines)

    while (!quit) {
      board.restore(startBoard)
      position = Position(startPosition)
      var startPieceMove = nextPiece
      var bestMove = evaluateBestMove(startPieceMove)

      while (!quit && bestMove.isDefined) {
        waitIfPaused(startPieceMove.piece)
        if (doStep)
          animateMove(startPieceMove, bestMove.get)

        startPieceMove = nextPiece
        bestMove = makeMove(startPieceMove, bestMove.get)
        moves += 1
        movesTotal += 1
      }
      if (!quit) {
        newGame
      }
      moves = 0
      clearedLines = 0
    }
    exit()
  }

  private def newGame() {
    games += 1

    totalClearedLines += clearedLines

    if (minClearedLines == 0 || clearedLines < minClearedLines)
      minClearedLines = clearedLines

    if (maxClearedLines == 0 || clearedLines > maxClearedLines)
      maxClearedLines = clearedLines

    gameEventReceiver.setNumberOfGamesAndLinesInLastGame(games, clearedLines, totalClearedLines, minClearedLines, maxClearedLines)
  }

  private def nextPiece = allValidPieceMovesForEmptyBoard.startMoveForPiece(pieceGenerator.nextPiece)

  private def waitIfPaused(startPiece: Piece) {
    if (paused && !quit) {
      updatePositionInGUI(startPiece)
      updateGameInfoInGUI
    }

    while (paused && !doStep && !quit)
      Thread.sleep(20)
  }

  private def makeMove(startPieceMove: PieceMove, pieceMove: PieceMove): Option[PieceMove] = {
    val clearedLines: Long = pieceMove.setPiece

    // Update GUI every 100 piece and always if in step mode
    if (doStep || movesTotal % 100 == 0) {
      if (!doStep)
        updatePositionInGUI(pieceMove.piece)
      updateGameInfoInGUI()
    }
    setPieceOnPosition(pieceMove.piece, pieceMove.move, clearedLines)

    doStep = timeToShowStepMilisec == MoveStep.FastStepTime
    timeToShowStepMilisec = MoveStep.DefaultStepTime
    this.clearedLines += clearedLines
    clearedLinesTotal += clearedLines

    evaluateBestMove(startPieceMove)
  }

  private def setPieceOnPosition(piece: Piece, move: Move, clearedLines: Long) {
    position.setPiece(piece, move)
    if (clearedLines > 0) {
      val pieceHeight = piece.height(move.rotation)
      if (doStep)
        position.animateClearedLines(move.y, pieceHeight, gameEventReceiver, timeToShowStepMilisec)
      position.clearLines(move.y, pieceHeight)
    }
  }

  private def evaluateBestMove(startPieceMove: PieceMove): Option[PieceMove] = {
    if (startPieceMove.isFree) {
      val validMoves = ValidMoves(board).pieceMoves(startPieceMove)
      EvaluatedMoves(board, validMoves, boardEvaluator, allValidPieceMovesForEmptyBoard.startPieces, settings.firstFreeRowUnderStartPiece, maxEquity).bestMove
    } else {
      None
    }
  }

  private def animateMove(startPieceMove: PieceMove, pieceMove: PieceMove) {
    startPieceMove.prepareAnimatedPath
    startPieceMove.calculateAnimatedPath(null, 0, 0)

    val animatedPosition = Position(position)

    var step = pieceMove
    var steps = List.empty[PieceMove]
    while (step != null) {
      steps = step :: steps
      step = step.animatedPath
    }

    steps.foreach(step => {
      val animatedPosition = Position(position)
      animatedPosition.setPiece(step.piece, step.move)
      gameEventReceiver.setPosition(animatedPosition)
      Thread.sleep(timeToShowStepMilisec)
    })
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

package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.piecegenerator.DefaultPieceGenerator
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.boardevaluator.{JTengstrandBoardEvaluator1DefaultSettings, BoardEvaluator, JTengstrandBoardEvaluator1}
import nu.tengstrand.tetrisanalyzer.gui.GameView
import startpiece.StartPieceGenerator
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings

object Game {
  def PausedAtStartup = true
}

class Game(timer: Timer, gameView: GameView) {
  private var boardSize = new BoardSize(new MinMax(10, 10, 10), new MinMax(20, 20, 20))
  private var boardSizeBeforeResize: Option[Size] = None

  private var board: Option[Board] = None
  private var position: Option[Position] = None
  private var resizingPosition: Option[Position] = None
  private val boardEvaluatorSettings = new JTengstrandBoardEvaluator1DefaultSettings

  private var boardEvaluator: Option[BoardEvaluator] = None
  private var computerPlayer: Option[ComputerPlayer] = None

  private var seed = 1L
  private var showNextPiece = true
  private var slidingEnabled = false
  private var showRankedMoves = false
  private val speed = new Speed()
  private var paused = Game.PausedAtStartup
  private var pausedBeforeResizing = paused
  private var pausedBeforeShowRankedMoves = paused
  private var pieceGenerator = new DefaultPieceGenerator(seed)
  private var startPieceGenerator = new StartPieceGenerator(pieceGenerator)

  private def boardWidth = boardSize.width.value
  private def boardHeight = boardSize.height.value

  startNewGameWithEmptyBoard()

  private def startNewGameWithEmptyBoard(): Unit = {
    board = Some(Board(boardWidth, boardHeight))
    position = Some(Position(boardWidth, boardHeight))
    resizingPosition = Some(Position(boardWidth, boardHeight))
    startNewGame()
  }

  private def startNewGame(): Unit = {
    computerPlayer.foreach(_.quitGame())

    val settings = new DefaultGameSettings
    val currentBoard = board.get
    val currentPosition = position.get

    gameView.setSeed(seed)
    gameView.setShowNextPiece(showNextPiece)
    val be = new JTengstrandBoardEvaluator1(boardEvaluatorSettings, currentBoard.width, currentBoard.height)
    boardEvaluator = Some(be)
    boardSize = new BoardSize(new MinMax(currentBoard.width, be.minBoardWidth, be.maxBoardWidth),
                              new MinMax(currentBoard.height, be.minBoardHeight, be.maxBoardHeight))
    val cp = new ComputerPlayer(speed, startPieceGenerator, currentBoard, currentPosition, be, settings, slidingEnabled, gameView)
    computerPlayer = Some(cp)
    cp.setShowNextPiece(showNextPiece)
    cp.setShowRankedMoves(showRankedMoves)
    cp.setPaused(paused)
    timer.reset()
    cp.start()
    gameView.stopResizingBoard(currentPosition, showRankedMoves)
  }

  def performMove(): Unit = { computerPlayer.foreach(_.performStep()) }

  def performRankedMove(): Unit = { computerPlayer.foreach(_.performStep()) }

  def togglePause(): Unit = {
    if (paused && showRankedMoves) {
      pausedBeforeShowRankedMoves = false
      showRankedMoves(false)
    } else {
      setPaused(!paused)
    }
  }

  private def setPaused(paused: Boolean): Unit = {
    this.paused = paused
    timer.setPaused(paused)
    computerPlayer.foreach(_.setPaused(paused))
    gameView.setPaused(paused)
  }

  def toggleSliding(): Unit = {
    slidingEnabled = !slidingEnabled
    gameView.setSliding(slidingEnabled)
    computerPlayer.foreach(_.setSliding(slidingEnabled))
  }

  def toggleShowNextPiece(): Unit = {
    showNextPiece = !showNextPiece
    gameView.setShowNextPiece(showNextPiece)
    computerPlayer.foreach(_.setShowNextPiece(showNextPiece))
  }

  def increaseSpeed(): Unit = { computerPlayer.foreach(_.increaseSpeed()) }

  def decreaseSpeed(): Unit = { computerPlayer.foreach(_.decreaseSpeed()) }

  def increaseSeed(): Unit = {
    seed += 1
    restartPieceGenerator()
  }

  def decreaseSeed(): Unit = {
    if (seed > 0) {
      seed -= 1
      restartPieceGenerator()
    }
  }

  private def restartPieceGenerator(): Unit = {
    pieceGenerator = new DefaultPieceGenerator(seed)
    computerPlayer.foreach(_.setPieceGenerator(pieceGenerator))
    gameView.setSeed(seed)
  }

  def startResizeBoard(): Unit = {
    boardSizeBeforeResize = Some(boardSize.size)
    pausedBeforeResizing = paused
    setPaused(true);
    updateBoardSize()
  }

  def acceptBoardSize(): Unit = {
    setPaused(pausedBeforeResizing)
    if (hasBoardSizeChanged)
      startNewGameWithEmptyBoard()
    else
      position.foreach(p => gameView.stopResizingBoard(p, showRankedMoves))
  }

  private def hasBoardSizeChanged = boardSizeBeforeResize.exists(_ != boardSize.size)

  def abortBoardSize(): Unit = {
    setPaused(pausedBeforeResizing)
    position.foreach(p => gameView.stopResizingBoard(p, showRankedMoves))
  }

  def decreaseBoardWidth(): Unit = {
    boardSize.decreaseWidth()
    updateBoardSize()
  }

  def increaseBoardWidth(): Unit = {
    boardSize.increaseWidth()
    updateBoardSize()
  }

  def decreaseBoardHeight(): Unit = {
    boardSize.decreaseHeight()
    updateBoardSize()
  }

  def increaseBoardHeight(): Unit = {
    boardSize.increaseHeight()
    updateBoardSize()
  }

  def increaseBoardWidthKeepRatio(): Unit = {
    boardSize.increaseSizeKeepRatio()
    updateBoardSize()
  }

  def decreaseBoardWidthKeepRatio(): Unit = {
    boardSize.decreaseSizeKeepRatio()
    updateBoardSize()
  }

  private def updateBoardSize(): Unit = {
    resizingPosition = Some(Position(boardWidth, boardHeight))
    resizingPosition.foreach(p => gameView.setResizingBoard(p, boardWidth, boardHeight))
  }

  def toggleMaxSpeed(): Unit = { computerPlayer.foreach(_.toggleMaxSpeed()) }

  def isRankedMovesVisible = showRankedMoves

  def showRankedMoves(show: Boolean): Unit = {
    if (show) {
      pausedBeforeShowRankedMoves = paused
      setPaused(true)
    } else {
      setPaused(pausedBeforeShowRankedMoves)
    }

    showRankedMoves = show
    computerPlayer.foreach(_.setShowRankedMoves(showRankedMoves))
    gameView.showRankedMoves(show)
  }
}

package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.piecegenerator.DefaultPieceGenerator
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.boardevaluator.{JTengstrandBoardEvaluator1DefaultSettings, BoardEvaluator, JTengstrandBoardEvaluator1}
import nu.tengstrand.tetrisanalyzer.gui.GameView
import startpiece.StartPieceGenerator
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings

object Game {
  def PausedOnStartup = true
}

class Game(timer: Timer, gameView: GameView) {
  private var boardSize = new BoardSize(new MinMax(10, 10, 10), new MinMax(20, 20, 20))
  private var boardSizeBeforeResize: Size = null

  private var board: Board = null
  private var position: Position = null
  private var resizingPosition: Position = null
  private val boardEvaluatorSettings = new JTengstrandBoardEvaluator1DefaultSettings

  private var boardEvaluator: BoardEvaluator = null
  private var computerPlayer: ComputerPlayer = null

  private var seed = 1L
  private var showNextPiece = false
  private var slidingEnabled = false
  private var showRankedMoves = false
  private val speed = new Speed()
  private var paused = Game.PausedOnStartup
  private var pausedBeforeResizing = paused
  private var pausedBeforeShowRankedMoves = paused
  private var pieceGenerator = new DefaultPieceGenerator(seed)
  private var startPieceGenerator = new StartPieceGenerator(pieceGenerator)

  startNewGameWithEmptyBoard()

  private def boardWidth = boardSize.width.value
  private def boardHeight = boardSize.height.value

  private def startNewGameWithEmptyBoard() {
    board = Board(boardWidth, boardHeight)
    position = Position(boardWidth, boardHeight)
    resizingPosition = Position(boardWidth, boardHeight)
    startNewGame()
  }

  private def startNewGame() {
    if (computerPlayer != null)
      computerPlayer.quitGame()

    val settings = new DefaultGameSettings

    gameView.setSeed(seed)
    gameView.setShowNextPiece(showNextPiece)
    boardEvaluator = new JTengstrandBoardEvaluator1(boardEvaluatorSettings, board.width, board.height)
    boardSize = new BoardSize(new MinMax(board.width, boardEvaluator.minBoardWidth, boardEvaluator.maxBoardWidth),
                              new MinMax(board.height, boardEvaluator.minBoardHeight, boardEvaluator.maxBoardHeight))
    computerPlayer = new ComputerPlayer(speed, startPieceGenerator, board, position, boardEvaluator, settings, slidingEnabled, gameView)
    computerPlayer.setShowNextPiece(showNextPiece)
    computerPlayer.setShowRankedMoves(showRankedMoves)
    computerPlayer.setPaused(paused)
    timer.reset()
    computerPlayer.start()
    gameView.stopResizingBoard(position, showRankedMoves)
  }

  def performMove() { computerPlayer.performStep() }

  def performRankedMove() { computerPlayer.performStep() }

  def togglePause() {
    if (paused && showRankedMoves) {
      pausedBeforeShowRankedMoves = false
      showRankedMoves(false)
    } else {
      setPaused(!paused)
    }
  }

  private def setPaused(paused: Boolean) {
    this.paused = paused
    timer.setPaused(paused)
    computerPlayer.setPaused(paused)
    gameView.setPaused(paused)
  }

  def toggleSliding() {
    slidingEnabled = !slidingEnabled
    gameView.setSliding(slidingEnabled)
    computerPlayer.setSliding(slidingEnabled)
  }

  def toggleShowNextPiece() {
    showNextPiece = !showNextPiece
    gameView.setShowNextPiece(showNextPiece)
    computerPlayer.setShowNextPiece(showNextPiece)
  }

  def increaseSpeed() { computerPlayer.increaseSpeed() }

  def decreaseSpeed() { computerPlayer.decreaseSpeed() }

  def increaseSeed() {
    changeSeedAndStartNewGame(1)
  }

  def decreaseSeed() {
    if (seed > 0)
      changeSeedAndStartNewGame(-1)
  }

  private def changeSeedAndStartNewGame(seedAdd: Int) {
    seed += seedAdd
    gameView.setSeed(seed)
    pieceGenerator = new DefaultPieceGenerator(seed)
    startNewGame()
  }

  def startResizeBoard() {
    boardSizeBeforeResize = boardSize.size
    pausedBeforeResizing = paused
    setPaused(true);
    updateBoardSize()
  }

  def acceptBoardSize() {
    setPaused(pausedBeforeResizing)
    if (hasBoardSizeChanged)
      startNewGameWithEmptyBoard()
    else
      gameView.stopResizingBoard(position, showRankedMoves)
  }

  private def hasBoardSizeChanged = boardSize.size != boardSizeBeforeResize

  def abortBoardSize() {
    setPaused(pausedBeforeResizing)
    gameView.stopResizingBoard(position, showRankedMoves)
  }

  def decreaseBoardWidth() {
    boardSize.decreaseWidth()
    updateBoardSize()
  }

  def increaseBoardWidth() {
    boardSize.increaseWidth()
    updateBoardSize()
  }

  def decreaseBoardHeight() {
    boardSize.decreaseHeight()
    updateBoardSize()
  }

  def increaseBoardHeight() {
    boardSize.increaseHeight()
    updateBoardSize()
  }

  def increaseBoardWidthKeepRatio() {
    boardSize.increaseSizeKeepRatio()
    updateBoardSize()
  }

  def decreaseBoardWidthKeepRatio() {
    boardSize.decreaseSizeKeepRatio()
    updateBoardSize()
  }

  private def updateBoardSize() {
    resizingPosition = Position(boardWidth, boardHeight)
    gameView.setResizingBoard(resizingPosition, boardWidth, boardHeight)
  }

  def toggleMaxSpeed() { computerPlayer.toggleMaxSpeed() }

  def isRankedMovesVisible = showRankedMoves

  def showRankedMoves(show: Boolean) {
    if (show) {
      pausedBeforeShowRankedMoves = paused
      setPaused(true)
    } else {
      setPaused(pausedBeforeShowRankedMoves)
    }

    showRankedMoves = show
    computerPlayer.setShowRankedMoves(showRankedMoves)
    gameView.showRankedMoves(show)
  }
}

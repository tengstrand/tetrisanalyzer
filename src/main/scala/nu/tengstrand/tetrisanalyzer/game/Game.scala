package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.piecegenerator.DefaultPieceGenerator
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.settings.SpecifiedGameSettings
import nu.tengstrand.tetrisanalyzer.boardevaluator.{JTengstrandBoardEvaluator1DefaultSettings, BoardEvaluator, JTengstrandBoardEvaluator1}
import nu.tengstrand.tetrisanalyzer.gui.GameView

object Game {
  def PausedOnStartup = true
}

class Game(timer: Timer, gameView: GameView) {
  private var boardWidth = 10
  private var boardHeight = 20

  private var boardEvaluator: BoardEvaluator = null
  private var computerPlayer: ComputerPlayer = null

  private var seed = 2L
  private var slidingEnabled = false
  private val speed = new Speed()
  private var paused = Game.PausedOnStartup

  startNewGame()

  private def startNewGame() {
    if (computerPlayer != null)
      computerPlayer.quitGame()

    val board = Board(boardWidth, boardHeight)
    val position = Position(boardWidth, boardHeight)
    val settings = new SpecifiedGameSettings(slidingEnabled, seed)
    val pieceGenerator = new DefaultPieceGenerator
    val boardEvaluatorSettings = new JTengstrandBoardEvaluator1DefaultSettings

    boardEvaluator = new JTengstrandBoardEvaluator1(boardEvaluatorSettings, board.width, board.height)
    computerPlayer = new ComputerPlayer(speed, board, position, boardEvaluator, pieceGenerator, settings, gameView)
    timer.reset()
    computerPlayer.start()
    computerPlayer.setPaused(paused)
  }

  def performMove() {
    computerPlayer.performStep()
  }

  def togglePause() {
    paused = !paused
    timer.setPaused(paused)
    computerPlayer.setPaused(paused)
    gameView.setPaused(paused)
  }

  def toggleSliding() {
    slidingEnabled = !slidingEnabled
    startNewGame()
  }

  def increaseSpeed() {
    computerPlayer.increaseSpeed()
  }

  def decreaseSpeed() {
    computerPlayer.decreaseSpeed()
  }

  def increaseSeed() {
    seed += 1
    startNewGame()
  }

  def decreaseSeed() {
    if (seed > 0) {
      seed -= 1
      startNewGame()
    }
  }

  def decreaseBoardWidth() {
    if (boardWidth > boardEvaluator.minBoardHeight) {
      boardWidth -= 1
      startNewGame()
    }
  }

  def increaseBoardWidth() {
    if (boardWidth < boardEvaluator.maxBoardWidth) {
      boardWidth += 1
      startNewGame()
    }
  }

  def decreaseBoardHeight() {
    if (boardHeight > boardEvaluator.minBoardWidth) {
      boardHeight -= 1
      startNewGame()
    }
  }

  def increaseBoardHeight() {
    if (boardHeight < boardEvaluator.maxBoardHeight) {
      boardHeight += 1
      startNewGame()
    }
  }

  def toggleMaxSpeed() {
    computerPlayer.toggleMaxSpeed()
  }

  def toggleSmallBoard() {
    gameView.toggleSmallBoard()
  }

  def toggleShowGameInfo() {
    gameView.toggleShowGameInfo()
  }

  def toggleShowHelp() {
    gameView.toggleShowHelp()
  }

  def toggleShowRankedMoves() {
    gameView.toggleShowRankedMoves()
  }
}

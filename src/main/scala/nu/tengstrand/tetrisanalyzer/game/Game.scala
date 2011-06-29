package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.piecegenerator.DefaultPieceGenerator
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.settings.SpecifiedGameSettings
import nu.tengstrand.tetrisanalyzer.boardevaluator.{JTengstrandBoardEvaluator1DefaultSettings, BoardEvaluator, JTengstrandBoardEvaluator1}

class Game(timer: Timer, gameEventReceiver: GameEventReceiver) {
  private var boardWidth = 10
  private var boardHeight = 20

  private var boardEvaluator: BoardEvaluator = null
  private var computerPlayer: ComputerPlayer = null

  private var seed = 0
  private var slidingEnabled = false
  private var paused = true

  startNewGame()

  private def startNewGame() {
    if (computerPlayer != null)
      computerPlayer.quitGame

    val board = Board(boardWidth, boardHeight)
    val position = Position(boardWidth, boardHeight)
    val settings = new SpecifiedGameSettings(slidingEnabled, seed)
    val pieceGenerator = new DefaultPieceGenerator(seed)
    val boardEvaluatorSettings = new JTengstrandBoardEvaluator1DefaultSettings

    boardEvaluator = new JTengstrandBoardEvaluator1(boardEvaluatorSettings, board.width, board.height)
    computerPlayer = new ComputerPlayer(paused, board, position, boardEvaluator, pieceGenerator, settings, gameEventReceiver)
    timer.reset
    computerPlayer.start
  }

  def performMove() {
    if (paused)
      computerPlayer.performStep()
  }

  def togglePause() {
    paused = !paused
    timer.setPaused(paused)
    computerPlayer.setPaused(paused)
    gameEventReceiver.setPaused(paused)
  }

  def toggleSliding() {
    slidingEnabled = !slidingEnabled
    startNewGame()
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
}

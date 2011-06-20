package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.boardevaluator.JTengstrandBoardEvaluator1
import nu.tengstrand.tetrisanalyzer.piecegenerator.DefaultPieceGenerator
import nu.tengstrand.tetrisanalyzer.gui.{GameInfoView, PositionView}
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.settings.SpecifiedGameSettings

class Game(timer: Timer, positionView: PositionView, gameInfoView: GameInfoView) {
  private var boardWidth = 10
  private var boardHeight = 20
  private val gameEventReceiver = new GameEventDelegate(positionView, gameInfoView)

  private var computerPlayer: ComputerPlayer = null

  private var slidingEnabled = false
  private var paused = true

  startNewGame()

  private def startNewGame() {
    if (computerPlayer != null)
      computerPlayer.quitGame

    val board = Board(boardWidth, boardHeight)
    val position = Position(boardWidth, boardHeight)
    val boardEvaluator = new JTengstrandBoardEvaluator1(board.width, board.height)
    val settings = new SpecifiedGameSettings(slidingEnabled)
    val pieceGenerator = new DefaultPieceGenerator(settings.pieceGeneratorSeed)

    computerPlayer = new ComputerPlayer(paused, board, position, boardEvaluator, pieceGenerator, settings, gameEventReceiver)
    timer.reset
    computerPlayer.start
  }

  def performMove() { computerPlayer.performStep() }

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

  def decreaseBoardWidth {
    if (boardWidth > 4) {
      boardWidth -= 1
      startNewGame()
    }
  }

  def increaseBoardWidth {
    if (boardWidth < 10) {
      boardWidth += 1
      startNewGame()
    }
  }

  def decreaseBoardHeight {
    if (boardHeight > 4) {
      boardHeight -= 1
      startNewGame()
    }
  }

  def increaseBoardHeight {
    if (boardHeight < 20) {
      boardHeight += 1
      startNewGame()
    }
  }
}

package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.boardevaluator.JTengstrandBoardEvaluator1
import nu.tengstrand.tetrisanalyzer.piecegenerator.DefaultPieceGenerator
import nu.tengstrand.tetrisanalyzer.gui.{GameInfoView, PositionView}
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.settings.GameSettings
class Game(settings: GameSettings, timer: Timer, positionView: PositionView, gameInfoView: GameInfoView) {
  private var boardWidth = 10
  private var boardHeight = 20
  private val gameEventReceiver = new GameEventDelegate(positionView, gameInfoView)

  private var computerPlayer: ComputerPlayer = null

  updateBoardSize()

  private def updateBoardSize() {
    if (computerPlayer != null)
      computerPlayer.quitGame

    val board = Board(boardWidth, boardHeight)
    val position = Position(boardWidth, boardHeight)
    val boardEvaluator = new JTengstrandBoardEvaluator1(board.width, board.height)
    val pieceGenerator = new DefaultPieceGenerator(settings.pieceGeneratorSeed)

    computerPlayer = new ComputerPlayer(board, position, boardEvaluator, pieceGenerator, settings, gameEventReceiver)
    computerPlayer.start
  }

  def performMove() { computerPlayer.performStep() }

  def isPaused = timer.paused

  def pause() {
    timer.togglePause
    gameEventReceiver.setPaused(timer.paused)
    computerPlayer.setPaused(timer.paused)
  }

  def decreaseBoardWidth {
    if (boardWidth > 4) {
      boardWidth -= 1
      updateBoardSize()
    }
  }

  def increaseBoardWidth {
    if (boardWidth < 10) {
      boardWidth += 1
      updateBoardSize()
    }
  }

  def decreaseBoardHeight {
    if (boardHeight > 4) {
      boardHeight -= 1
      updateBoardSize()
    }
  }

  def increaseBoardHeight {
    if (boardHeight < 20) {
      boardHeight += 1
      updateBoardSize()
    }
  }
}

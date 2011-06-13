package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.boardevaluator.JTengstrandBoardEvaluator1
import nu.tengstrand.tetrisanalyzer.piecegenerator.DefaultPieceGenerator
import nu.tengstrand.tetrisanalyzer.gui.{GameInfoView, PositionView}
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.settings.{GameSettings, DefaultGameSettings}
class Game(settings: GameSettings, timer: Timer, positionView: PositionView, gameInfoView: GameInfoView) {
  private val board = Board()
  private val position = Position()
  private val boardEvaluator = new JTengstrandBoardEvaluator1(board.width, board.height)
  private var pieceGenerator = new DefaultPieceGenerator(settings.pieceGeneratorSeed)
  private val gameEventReceiver = new GameEventDelegate(positionView, gameInfoView)
  private var computerPlayer = new ComputerPlayer(board, position, boardEvaluator, pieceGenerator, settings, gameEventReceiver)

  computerPlayer.start

  private def changeBoardSize(boardWidth: Int, boardHeight: Int) {
    computerPlayer.quitGame

    val board = Board(boardWidth, boardHeight)
    val boardEvaluator = new JTengstrandBoardEvaluator1(board.width, board.height)
    val settings = new DefaultGameSettings
    val pieceGenerator = new DefaultPieceGenerator(settings.pieceGeneratorSeed)
    val position = Position()

    computerPlayer = new ComputerPlayer(board, position, boardEvaluator, pieceGenerator, settings, gameEventReceiver)
    computerPlayer.start
  }

  def performMove() { computerPlayer.performStep() }

  def pause() {
    timer.togglePause
    gameEventReceiver.setPaused(timer.paused)
    computerPlayer.setPaused(timer.paused)
  }



  def decreaseBoardWidth {
    if (board.width > 4) {
      changeBoardSize(board.width - 1, board.height)
    }
  }
}

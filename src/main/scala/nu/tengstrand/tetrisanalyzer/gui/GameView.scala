package nu.tengstrand.tetrisanalyzer.gui

import nu.tengstrand.tetrisanalyzer.settings.ColorSettings
import nu.tengstrand.tetrisanalyzer.game.{GameEventReceiver, ColoredPosition}
import nu.tengstrand.tetrisanalyzer.move.MoveEquity
import java.awt.{Graphics2D, Graphics, Dimension}

class GameView(colorSettings: ColorSettings) extends DoubleBufferedView with GameEventReceiver {
  private val positionView = new PositionView(colorSettings)
  private val gameInfoView = new GameInfoView
  private val rankedMovesView = new RankedMovesView
  private val helpView = new HelpView

  private var paused = false
  private var boardSize = new Dimension(0,0)

  private var showGameInfo = true
  private var showHelp = false
  private var showRankedMoves = false

  def toggleShowGameInfo() { showGameInfo = !showGameInfo }
  def toggleShowHelp() { showHelp = !showHelp }
  def toggleShowRankedMoves() { showRankedMoves = !showRankedMoves }

  def isPaused: Boolean = paused
  def setPaused(paused: Boolean) { this.paused = paused; gameInfoView.setPaused(paused) }

  def setPosition(coloredPosition: ColoredPosition) { positionView.setPosition(coloredPosition) }
  def isReadyToReceivePosition: Boolean = { positionView.isReadyToReceivePosition }

  def setSeed(seed: Long) { gameInfoView.setSeed(seed) }
  def setSpeed(speedIndex: Int, isMaxSpeed: Boolean) { gameInfoView.setSpeed(speedIndex, isMaxSpeed) }
  def setSliding(enabled: Boolean) { gameInfoView.setSliding(enabled) }
  def setBoardSize(width: Int, height: Int) { gameInfoView.setBoardSize(width, height) }
  def setNumberOfPieces(pieces: Long) { gameInfoView.setNumberOfPieces(pieces) }
  def setTotalNumberOfPieces(pieces: Long) { gameInfoView.setTotalNumberOfPieces(pieces) }
  def setNumberOfClearedRows(rows: Long) { gameInfoView.setNumberOfClearedRows(rows)}
  def setTotalNumberOfClearedRows(rows: Long) { gameInfoView.setTotalNumberOfClearedRows(rows)}
  def setTimePassed(seconds: Double) { gameInfoView.setTimePassed(seconds) }
  def updateGui() { gameInfoView.updateGui() }
  def setNumberOfGamesAndRowsInLastGame(games: Long, rows: Long, totalClearedRows: Long, minRows: Long, maxRows: Long) {
    gameInfoView.setNumberOfGamesAndRowsInLastGame(games, rows, totalClearedRows, minRows, maxRows)
  }
  def setRankedMoves(rankedMoves: List[MoveEquity]) { rankedMovesView.setRankedMoves(rankedMoves) }

  def preparePaintGraphics: Dimension = {
    boardSize = positionView.preparePaintGraphics(size)
    size
  }

  def paintGraphics(graphics: Graphics) {
    var origoX = boardSize.width + 15

    positionView.paintGraphics(size, graphics)

    val g = graphics.asInstanceOf[Graphics2D];

    if (showGameInfo) {
      gameInfoView.paintGraphics(origoX, g)
      origoX += 240
    }

    if (showRankedMoves) {
      rankedMovesView.paintGraphics(origoX, g)
      origoX += 130
    }
    if (showHelp) {
      helpView.paintGraphics(origoX, graphics)
    }
  }
}
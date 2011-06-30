package nu.tengstrand.tetrisanalyzer.gui

import nu.tengstrand.tetrisanalyzer.settings.ColorSettings
import java.awt.{Graphics, Dimension}
import nu.tengstrand.tetrisanalyzer.game.{GameEventReceiver, ColoredPosition}

class GameView(colorSettings: ColorSettings) extends DoubleBufferedView with GameEventReceiver {
  private val positionView = new PositionView(colorSettings)
  private val gameInfoView = new GameInfoView
  private val helpView = new HelpView

  private var paused = false
  private var boardSize = new Dimension(0,0)

  private var showHelp = false

  def toggleShowHelp { showHelp = !showHelp }

  def isPaused: Boolean = paused
  def setPaused(paused: Boolean) { this.paused = paused; gameInfoView.setPaused(paused) }

  def setPosition(coloredPosition: ColoredPosition) { positionView.setPosition(coloredPosition) }
  def isReadyToReceivePosition: Boolean = { positionView.isReadyToReceivePosition }

  def setSeed(seed: Long) { gameInfoView.setSeed(seed) }
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

  def preparePaintGraphics: Dimension = {
    boardSize = positionView.preparePaintGraphics(size)
    size
  }

  def paintGraphics(g: Graphics) {
    val origoX = boardSize.width + 15

    positionView.paintGraphics(size, g)

    if (showHelp)
      helpView.paintGraphics(origoX, g)
    else
      gameInfoView.paintGraphics(origoX, g)
  }
}
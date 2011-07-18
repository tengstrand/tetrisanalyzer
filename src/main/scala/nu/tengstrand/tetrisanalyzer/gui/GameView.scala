package nu.tengstrand.tetrisanalyzer.gui

import nu.tengstrand.tetrisanalyzer.settings.ColorSettings
import nu.tengstrand.tetrisanalyzer.game.{GameEventReceiver, ColoredPosition}
import nu.tengstrand.tetrisanalyzer.move.MoveEquity
import java.awt.{Color, Graphics2D, Graphics, Dimension}
import rankedmove.RankedMovesView

class GameView(colorSettings: ColorSettings) extends DoubleBufferedView with GameEventReceiver {
  private val positionView = new PositionView(colorSettings)
  private val gameInfoView = new GameInfoView
  private val rankedMovesView = new RankedMovesView
  private val helpView = new HelpView

  private var paused = false
  private var boardSize = new Dimension(0,0)

  def toggleMiniatureBoard() { positionView.toggleMiniatureBoard() }
  def toggleShowGameInfo() { gameInfoView.toggleShowView() }
  def toggleShowHelp() { helpView.toggleShowView() }
  def toggleShowRankedMoves() { rankedMovesView.toggleShowView(); positionView.toggleShowNumbers() }

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
  def setRankedMoves(rankedMoves: List[MoveEquity], maxX: Int, maxY: Int) { rankedMovesView.setRankedMoves(rankedMoves, maxX, maxY) }

  def preparePaintGraphics: Dimension = {
    boardSize = positionView.preparePaintGraphics(size)
    size
  }

  def paintGraphics(graphics: Graphics) {
    var origoX = 15

    val g = graphics.asInstanceOf[Graphics2D];

    paintWhiteBackground(g)

    positionView.setShowRowNumbers(rankedMovesView.showRowNumbers)
    positionView.paintGraphics(size, g)
    origoX += boardSize.width

    gameInfoView.paintGameInfo(origoX, g)
    origoX += gameInfoView.width

    rankedMovesView.paintRankedMoves(origoX, g)
    origoX += rankedMovesView.width

    helpView.paintHelp(origoX, g)
  }

  private def paintWhiteBackground(g: Graphics2D) {
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, size.width, size.height)
  }
}
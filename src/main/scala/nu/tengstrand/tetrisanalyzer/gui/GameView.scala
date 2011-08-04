package nu.tengstrand.tetrisanalyzer.gui

import help.HelpView
import nu.tengstrand.tetrisanalyzer.settings.ColorSettings
import java.awt.{Color, Graphics2D, Graphics, Dimension}
import position.PositionView
import rankedmove.{RankedMoves, RankedMovesView}
import nu.tengstrand.tetrisanalyzer.move.Move
import nu.tengstrand.tetrisanalyzer.game.{GameEventReceiver, ColoredPosition}

class GameView(colorSettings: ColorSettings) extends DoubleBufferedView with GameEventReceiver {
  private val positionView = new PositionView(colorSettings)
  private val resizeBoardView = new PositionView(colorSettings)
  private val gameInfoView = new GameInfoView
  private val rankedMovesView = new RankedMovesView
  private val helpView = new HelpView

  private var paused = true
  private var boardSizeInPixels = new Dimension(0,0)

  private var isResizingBoard = false

  def setShowNextPiece(show: Boolean) { gameInfoView.setShowNextPiece(show) }

  def selectedRankedMove: Option[Move] = rankedMovesView.selectedMove

  def selectNextRankedMove() { rankedMovesView.selectNextRankedMove() }
  def selectPreviousRankedMove() { rankedMovesView.selectPreviousRankedMove() }

  def toggleMiniatureBoard() { positionView.toggleMiniatureBoard() }
  def toggleShowGameInfo() { gameInfoView.toggleShowView() }
  def setResizingBoard(coloredPosition: ColoredPosition, boardWidth: Int, boardHeight: Int) {
    isResizingBoard = true;
    resizeBoardView.setPosition(coloredPosition)
    helpView.setBoardSize(boardWidth, boardHeight)
    helpView.setView(isResizingBoard, false)
  }
  def stopResizingBoard(position: ColoredPosition, showRankedMoves: Boolean) {
    positionView.forceSetPosition(position)
    resizeBoardView.setPosition(position)

    isResizingBoard = false
    helpView.setView(false, showRankedMoves)
  }
  def toggleShowHelp() { helpView.toggleShowView() }

  def showRankedMoves(show: Boolean) {
    rankedMovesView.showRankedMoves(show);
    rankedMovesView.showCursor(paused)
    positionView.showNumbers(show);
    helpView.setView(isResizingBoard, show)
  }
  def isPaused: Boolean = paused
  def setPaused(paused: Boolean) {
    this.paused = paused;
    gameInfoView.setPaused(paused)
    rankedMovesView.showCursor(paused)
    helpView.setPaused(paused)
  }

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
  def setNumberOfGamesAndRowsInLastGame(games: Long, rows: Long, totalClearedRows: Long, minRows: Long, maxRows: Long) {
    gameInfoView.setNumberOfGamesAndRowsInLastGame(games, rows, totalClearedRows, minRows, maxRows)
  }
  def setRankedMoves(rankedMoves: RankedMoves) { rankedMovesView.setRankedMoves(rankedMoves) }

  def preparePaintGraphics: Dimension = {
    if (isResizingBoard)
      boardSizeInPixels = resizeBoardView.preparePaintGraphics(size)
    else
      boardSizeInPixels = positionView.preparePaintGraphics(size)

    size
  }

  def paintGraphics(graphics: Graphics) {
    var origoX = 15

    val g = graphics.asInstanceOf[Graphics2D];

    paintWhiteBackground(g)

    if (isResizingBoard) {
      resizeBoardView.paintGraphics(size, g)
    } else {
      positionView.setShowRowNumbers(rankedMovesView.showRowNumbers)
      positionView.paintGraphics(size, g)
    }
    origoX += boardSizeInPixels.width

    if (!isResizingBoard) {
      gameInfoView.paintGameInfo(origoX, g)
      origoX += gameInfoView.width

      rankedMovesView.paintRankedMoves(origoX, g)
      origoX += rankedMovesView.width
    }
    helpView.paintHelp(origoX, g)
  }

  private def paintWhiteBackground(g: Graphics2D) {
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, size.width, size.height)
  }
}
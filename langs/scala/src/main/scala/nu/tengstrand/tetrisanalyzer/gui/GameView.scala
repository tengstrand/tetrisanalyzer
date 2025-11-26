package nu.tengstrand.tetrisanalyzer.gui

import help.HelpView
import nu.tengstrand.tetrisanalyzer.settings.ColorSettings
import java.awt.{Color, Graphics2D, Graphics, Dimension}
import position.{PositionStartupHelpPainter, PositionView}
import rankedmove.{RankedMoves, RankedMovesView}
import nu.tengstrand.tetrisanalyzer.game.{GameEventReceiver, ColoredPosition}

class GameView(colorSettings: ColorSettings) extends DoubleBufferedView with GameEventReceiver {
  private val positionView = new PositionView(colorSettings)
  private val positionStartupHelpPainter = new PositionStartupHelpPainter
  private val resizeBoardView = new PositionView(colorSettings)
  private val gameInfoView = new GameInfoView
  private val rankedMovesView = new RankedMovesView
  private val helpView = new HelpView

  private var paused = true
  private var showStartupHelp = true
  private var boardSizeInPixels = new Dimension(0,0)

  private var isResizingBoard = false

  def hideStartupHelp(): Unit = { showStartupHelp = false }

  def setShowNextPiece(show: Boolean): Unit = { gameInfoView.setShowNextPiece(show) }

  def selectNextRankedMove(): Unit = { rankedMovesView.selectNextRankedMove() }
  def selectPreviousRankedMove(): Unit = { rankedMovesView.selectPreviousRankedMove() }

  def toggleMiniatureBoard(): Unit = { positionView.toggleMiniatureBoard() }
  def toggleShowGameInfo(): Unit = { gameInfoView.toggleShowView() }
  def setResizingBoard(coloredPosition: ColoredPosition, boardWidth: Int, boardHeight: Int): Unit = {
    isResizingBoard = true;
    resizeBoardView.setPosition(coloredPosition)
    helpView.setBoardSize(boardWidth, boardHeight)
    helpView.setView(isResizingBoard, false)
  }
  def stopResizingBoard(position: ColoredPosition, showRankedMoves: Boolean): Unit = {
    positionView.forceSetPosition(position)
    resizeBoardView.setPosition(position)

    isResizingBoard = false
    helpView.setView(false, showRankedMoves)
  }
  def toggleShowHelp(): Unit = { helpView.toggleShowView() }

  def showRankedMoves(show: Boolean): Unit = {
    rankedMovesView.showRankedMoves(show);
    rankedMovesView.showCursor(paused)
    positionView.showNumbers(show);
    helpView.setView(isResizingBoard, show)
  }
  def isPaused: Boolean = paused
  def setPaused(paused: Boolean): Unit = {
    this.paused = paused;
    gameInfoView.setPaused(paused)
    rankedMovesView.showCursor(paused)
    helpView.setPaused(paused)
  }

  def setPosition(coloredPosition: ColoredPosition): Unit = { positionView.setPosition(coloredPosition) }
  def isReadyToReceivePosition: Boolean = { positionView.isReadyToReceivePosition }

  def setSeed(seed: Long): Unit = { gameInfoView.setSeed(seed) }
  def setSpeed(speedIndex: Int, isMaxSpeed: Boolean): Unit = { gameInfoView.setSpeed(speedIndex, isMaxSpeed) }
  def setSliding(enabled: Boolean): Unit = { gameInfoView.setSliding(enabled) }
  def setBoardSize(width: Int, height: Int): Unit = { gameInfoView.setBoardSize(width, height) }
  def setNumberOfPieces(pieces: Long): Unit = { gameInfoView.setNumberOfPieces(pieces) }
  def setTotalNumberOfPieces(pieces: Long): Unit = { gameInfoView.setTotalNumberOfPieces(pieces) }
  def setNumberOfClearedRows(rows: Long): Unit = { gameInfoView.setNumberOfClearedRows(rows)}
  def setTotalNumberOfClearedRows(rows: Long): Unit = { gameInfoView.setTotalNumberOfClearedRows(rows)}
  def setTimePassed(seconds: Double): Unit = { gameInfoView.setTimePassed(seconds) }
  def setNumberOfGamesAndRowsInLastGame(games: Long, rows: Long, totalClearedRows: Long, minRows: Long, maxRows: Long): Unit = {
    gameInfoView.setNumberOfGamesAndRowsInLastGame(games, rows, totalClearedRows, minRows, maxRows)
  }
  def setRankedMoves(rankedMoves: RankedMoves): Unit = { rankedMovesView.setRankedMoves(rankedMoves) }

  def preparePaintGraphics: Dimension = {
    if (isResizingBoard)
      boardSizeInPixels = resizeBoardView.preparePaintGraphics(size)
    else
      boardSizeInPixels = positionView.preparePaintGraphics(size)

    size
  }

  def paintGraphics(graphics: Graphics): Unit = {
    var origoX = 15

    val g = graphics.asInstanceOf[Graphics2D];

    paintWhiteBackground(g)

    if (isResizingBoard) {
      resizeBoardView.paintGraphics(size, g)
    } else {
      positionView.setShowRowNumbers(rankedMovesView.showRowNumbers)
      positionView.paintGraphics(size, g)
      if (showStartupHelp)
        positionStartupHelpPainter.paintGraphics((boardSizeInPixels.width * 0.44).intValue(), g)
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

  private def paintWhiteBackground(g: Graphics2D): Unit = {
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, size.width, size.height)
  }
}
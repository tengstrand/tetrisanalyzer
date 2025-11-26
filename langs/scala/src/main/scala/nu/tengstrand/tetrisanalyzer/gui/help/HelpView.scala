package nu.tengstrand.tetrisanalyzer.gui.help

import java.awt.Graphics2D
import nu.tengstrand.tetrisanalyzer.gui.TextPainter

class HelpView extends TextPainter {
  private var isResizingBoard = false
  private var showView = false
  private val resizeBoardHelpView = new ResizeBoardHelpView
  private val gameHelpView = new GameHelpView
  private val rankedMovesHelpView = new RankedMovesHelpView
  private var helpPainter: HelpPainter = gameHelpView

  def setView(isResizingBoard: Boolean, showRankedMoves: Boolean): Unit = {
    this.isResizingBoard = isResizingBoard

    if (isResizingBoard)
      helpPainter = resizeBoardHelpView
    else if (showRankedMoves)
      helpPainter = rankedMovesHelpView
    else
      helpPainter = gameHelpView
  }

  def setPaused(paused: Boolean): Unit = { gameHelpView.setPaused(paused) }

  def toggleShowView(): Unit = { showView = !showView }

  def setBoardSize(width: Int, height: Int): Unit = { resizeBoardHelpView.setBoardSize(width, height)}

  def paintHelp(origoX: Int, g: Graphics2D): Unit = {
    if (showView || isResizingBoard)
      paintGraphics(origoX, g)
  }

  private def paintGraphics(origoX: Int, g: Graphics2D): Unit = {
    helpPainter.paintGraphics(origoX, g)
  }
}
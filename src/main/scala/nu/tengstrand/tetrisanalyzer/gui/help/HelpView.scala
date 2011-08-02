package nu.tengstrand.tetrisanalyzer.gui.help

import java.awt.Graphics2D
import nu.tengstrand.tetrisanalyzer.gui.TextDrawer

class HelpView extends TextDrawer {
  private var isResizingBoard = false
  private var showView = false
  private val resizeBoardHelpView = new ResizeBoardHelpView
  private val gameHelpView = new GameHelpView
  private val rankedMovesHelpView = new RankedMovesHelpView
  private var helpPainter: HelpPainter = gameHelpView

  def setView(isResizingBoard: Boolean, showRankedMoves: Boolean) {
    this.isResizingBoard = isResizingBoard
    println("isResize=" + isResizingBoard)

    if (isResizingBoard)
      helpPainter = resizeBoardHelpView
    else if (showRankedMoves)
      helpPainter = rankedMovesHelpView
    else
      helpPainter = gameHelpView
  }
  def toggleShowView() { showView = !showView }

  def setBoardSize(width: Int, height: Int) { resizeBoardHelpView.setBoardSize(width, height)}

  def paintHelp(origoX: Int, g: Graphics2D) {
    if (showView || isResizingBoard)
      paintGraphics(origoX, g)
  }

  private def paintGraphics(origoX: Int, g: Graphics2D) {
    helpPainter.paintGraphics(origoX, g)
  }
}
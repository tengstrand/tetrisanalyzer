package nu.tengstrand.tetrisanalyzer.gui.rankedmove

import nu.tengstrand.tetrisanalyzer.game.RankedMovesReceiver
import java.awt.{Color, Graphics2D}
import nu.tengstrand.tetrisanalyzer.gui.TextDrawer

class RankedMovesView extends TextDrawer with RankedMovesReceiver {
  private var rankedMoves: RankedMoves = null
  private var newRankedMoves: RankedMoves = null

  private var showView = false

  private val selectedRowColor = new Color(240, 240, 240)

  def showRankedMoves(show: Boolean) { showView = show }

  def isVisible = showView

  def showRowNumbers = rankedMoves != null && rankedMoves.hasDuplicatedVX

  def width = {
    if (rankedMoves == null || !showView)
      0
    else {
      rankedMoves.moves(0).asText.length() * 8 + 40
    }
  }

  def setRankedMoves(rankedMoves: RankedMoves) {
    newRankedMoves = rankedMoves

    if (this.rankedMoves == null)
      this.rankedMoves = newRankedMoves
  }

  def selectNextRankedMove() {
    if (rankedMoves != null)
      rankedMoves.selectNextMove()
  }

  def selectPreviousRankedMove() {
    if (rankedMoves != null)
      rankedMoves.selectPreviousMove()
  }

  def paintRankedMoves(origoX: Int, g: Graphics2D) {
    if (showView && rankedMoves != null)
      paintGraphics(origoX, g)
  }

  private def paintGraphics(origoX: Int, g: Graphics2D) {
    prepareDrawText(origoX, g, 7, Color.BLUE)
    drawHeader(g)
    drawSelectedRow(origoX, g)
    prepareDrawText(origoX, g, 10, Color.BLUE)
    drawRowNumbers(g)

    prepareDrawText(origoX + 28, g)
    drawMoves(g)
    rankedMoves = newRankedMoves
  }

  private def drawHeader(g: Graphics2D) { drawText(rankedMoves.headerAsText, 1, g) }

  private def drawRowNumbers(g: Graphics2D) {
    def rowNumber(row: Int) = if (row < 10) " " + row else row.toString

    for (row <- 1 to rankedMoves.moves.size)
      drawText(rowNumber(row), row + 1, g)
  }

  private def drawSelectedRow(x1: Int, g: Graphics2D) {
    val adjustY = 5
    val y1 = getY(rankedMoves.selectedRow + 1) + adjustY
    val width = 130
    val height = getY(rankedMoves.selectedRow + 2) + adjustY - y1 + 1
    g.setColor(selectedRowColor);
    g.fillRect(x1 - 5, y1, width, height)
  }

  private def drawMoves(g: Graphics2D) {
    rankedMoves.moves.foreach(rankedMove => drawText(rankedMove.asText, rankedMove.row + 1, g))
  }
}
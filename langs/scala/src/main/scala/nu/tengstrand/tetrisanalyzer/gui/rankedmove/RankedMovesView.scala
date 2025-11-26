package nu.tengstrand.tetrisanalyzer.gui.rankedmove

import nu.tengstrand.tetrisanalyzer.game.RankedMovesReceiver
import java.awt.{Color, Graphics2D}
import nu.tengstrand.tetrisanalyzer.gui.TextPainter
class RankedMovesView extends TextPainter with RankedMovesReceiver {
  private var rankedMoves: Option[RankedMoves] = None
  private var newRankedMoves: Option[RankedMoves] = None

  private var showCursor = false
  private var showView = false

  private val selectedRowColor = new Color(240, 240, 240)

  def showCursor(show: Boolean): Unit = { showCursor = show }

  def showRankedMoves(show: Boolean): Unit = { showView = show }

  def showRowNumbers = rankedMoves.exists(_.hasDuplicatedVX)

  def width = {
    rankedMoves match {
      case Some(rm) if !rm.isEmpty && showView => rm.moves(0).asText.length() * 8 + 40
      case _ => 0
    }
  }

  def setRankedMoves(rankedMoves: RankedMoves): Unit = {
    newRankedMoves = Some(rankedMoves)

    if (this.rankedMoves.isEmpty)
      this.rankedMoves = newRankedMoves
  }

  def selectNextRankedMove(): Unit = {
    rankedMoves.foreach(_.selectNextMove())
  }

  def selectPreviousRankedMove(): Unit = {
    rankedMoves.foreach(_.selectPreviousMove())
  }

  def paintRankedMoves(origoX: Int, g: Graphics2D): Unit = {
    if (showView && rankedMoves.isDefined)
      paintGraphics(origoX, g)
  }

  private def paintGraphics(origoX: Int, g: Graphics2D): Unit = {
    rankedMoves.foreach { rm =>
      prepareDrawText(origoX, g, 7, Color.BLUE)
      drawHeader(rm, g)

      if (showCursor)
        drawCursor(origoX, rm, g)
      prepareDrawText(origoX, g, 10, Color.BLUE)
      drawRowNumbers(rm, g)

      prepareDrawText(origoX + 28, g)
      drawMoves(rm, g)
    }
    rankedMoves = newRankedMoves
  }

  private def drawHeader(rankedMoves: RankedMoves, g: Graphics2D): Unit = { drawText(rankedMoves.headerAsText, 1, g) }

  private def drawRowNumbers(rankedMoves: RankedMoves, g: Graphics2D): Unit = {
    def rowNumber(row: Int) = if (row < 10) " " + row else row.toString

    for (row <- 1 to rankedMoves.moves.size)
      drawText(rowNumber(row), row + 1, g)
  }

  private def drawCursor(x1: Int, rankedMoves: RankedMoves, g: Graphics2D): Unit = {
    val adjustY = 5
    val y1 = getY(rankedMoves.selectedRow + 1) + adjustY
    val width = 130
    val height = getY(rankedMoves.selectedRow + 2) + adjustY - y1 + 1
    g.setColor(selectedRowColor);
    g.fillRect(x1 - 5, y1, width, height)
  }

  private def drawMoves(rankedMoves: RankedMoves, g: Graphics2D): Unit = {
    rankedMoves.moves.foreach(rankedMove => drawText(rankedMove.asText, rankedMove.row + 1, g))
  }
}
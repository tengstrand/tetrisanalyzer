package nu.tengstrand.tetrisanalyzer.gui.rankedmove

import nu.tengstrand.tetrisanalyzer.game.RankedMovesReceiver
import java.awt.{Color, Graphics2D}
import nu.tengstrand.tetrisanalyzer.move.MoveEquity
import nu.tengstrand.tetrisanalyzer.gui.TextDrawer

class RankedMovesView extends TextDrawer with RankedMovesReceiver {
  private var rankedMoves: RankedMoves = null
  private var newRankedMoves: RankedMoves = null

  private var showView = false

  def toggleShowView() { showView = !showView }

  def showRowNumbers = rankedMoves != null && rankedMoves.hasDuplicatedVX

  def width = {
    if (rankedMoves == null || !showView)
      0
    else {
      rankedMoves.moves(0).asText.length() * 8 + 40
    }
  }

  def setRankedMoves(moves: List[MoveEquity], maxX: Int, maxY: Int) {
    newRankedMoves = new RankedMoves(moves, maxX, maxY)

    if (rankedMoves == null)
      rankedMoves = newRankedMoves
  }

  def paintRankedMoves(origoX: Int, g: Graphics2D) {
    if (showView)
      paintGraphics(origoX, g)
  }

  private def paintGraphics(origoX: Int, g: Graphics2D) {
    if (rankedMoves != null) {
      prepareDraw(origoX, g, 7, Color.BLUE)
      drawHeader(g)
      prepareDraw(origoX, g, 10, Color.BLUE)
      drawNumbers(g)

      prepareDraw(origoX + 28, g)
      drawMoves(g)
      rankedMoves = newRankedMoves
    }
  }

  private def drawHeader(g: Graphics2D) { drawText(rankedMoves.headerAsText, 1, g) }

  private def drawNumbers(g: Graphics2D) {
    def rowNumber(row: Int) = if (row < 10) " " + row else row.toString

    for (row <- 1 to rankedMoves.moves.size)
      drawText(rowNumber(row), row + 1, g)
  }

  private def drawMoves(g: Graphics2D) {
    rankedMoves.moves.foreach(rankedMove => drawText(rankedMove.asText, rankedMove.row + 1, g))
  }
}
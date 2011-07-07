package nu.tengstrand.tetrisanalyzer.gui.rankedmove

import nu.tengstrand.tetrisanalyzer.game.RankedMovesReceiver
import java.awt.{Color, Graphics2D}
import nu.tengstrand.tetrisanalyzer.move.MoveEquity
import nu.tengstrand.tetrisanalyzer.gui.TextDrawer

class RankedMovesView extends TextDrawer with RankedMovesReceiver {
  private var rankedMoves: RankedMoves = null
  private var newRankedMoves: RankedMoves = null

  def setRankedMoves(moves: List[MoveEquity]) {
    newRankedMoves = new RankedMoves(moves)

    if (rankedMoves == null)
      rankedMoves = newRankedMoves
  }

  def paintGraphics(origoX: Int, g: Graphics2D) {
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
    val maxX = rankedMoves.maxX
    val maxY = rankedMoves.maxY
    val maxEquity = rankedMoves.maxEquity
    val hasDuplicatedVX = rankedMoves.hasDuplicatedVX

    for (i <- 0 until rankedMoves.size) {
      val isDuplicatedVX = i % 2 == 0
      val rankedMove = new RankedMove(i == 0, rankedMoves.moves(i), maxX, maxEquity)
      if (!hasDuplicatedVX)
        drawText(rankedMove.asText, i+2, g)
      else
        drawText(rankedMove.withYColumn(maxY, isDuplicatedVX).asText, i+2, g)
    }
  }
}
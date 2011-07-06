package nu.tengstrand.tetrisanalyzer.gui

import nu.tengstrand.tetrisanalyzer.game.RankedMovesReceiver
import java.awt.{Color, Graphics2D}
import nu.tengstrand.tetrisanalyzer.move.MoveEquity

class RankedMovesView extends TextDrawer with RankedMovesReceiver {
  private val helper = new RankedMovesViewHelper
  private var rankedMoves: List[MoveEquity] = null
  private var newRankedMoves: List[MoveEquity] = null

  def setRankedMoves(rankedMoves: List[MoveEquity]) {
    newRankedMoves = rankedMoves

    if (this.rankedMoves == null)
      this.rankedMoves = rankedMoves
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

  private def maxX = {
    rankedMoves.map(_.pieceMove.move.x).max + 1
  }

  private def drawHeader(g: Graphics2D) { drawText(helper.header(maxX), 1, g) }

  private def drawNumbers(g: Graphics2D) {
    def rowNumber(row: Int) = if (row < 10) " " + row else row.toString

    for (row <- 1 to rankedMoves.size)
      drawText(rowNumber(row), row + 1, g)
  }

  private def drawMoves(g: Graphics2D) {
    val maxEquity = rankedMoves.map(_.equity).max

    for (i <- 0 until rankedMoves.size) {
      drawText(helper.rowInfo(i == 0, rankedMoves(i), maxX, maxEquity), i+2, g)
    }
  }
}
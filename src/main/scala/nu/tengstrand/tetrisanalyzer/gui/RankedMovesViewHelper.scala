package nu.tengstrand.tetrisanalyzer.gui

import nu.tengstrand.tetrisanalyzer.move.{Move, MoveEquity}
import java.util.Locale

class RankedMovesViewHelper {
  def header(maxX: Int) = {
    if (maxX < 10)
      "    v x  Depth 0"
    else
      "    v  x  Depth 0"
  }

  def rowInfo(firstRow: Boolean, rankedMove: MoveEquity, maxX: Int, maxEquity: Double): String = {
    val move = rankedMove.pieceMove.move
    val equityWidth = maxEquity.toInt.toString.length + 5
    val delta = if (firstRow) " " else "+"

    rotationAndX(move, maxX) + "  " + alignRight(delta + withThreeDecimals(rankedMove.equity), equityWidth)
  }

  def rotationAndX(move: Move, maxX: Int) = move.rotation + " " + alignRight((move.x + 1).toString, maxX.toString.length)

  def withThreeDecimals(equity: Double): String = {
    "%.3f".formatLocal(Locale.ENGLISH, equity)
  }

  def alignRight(text: String, width: Int) = if (text.length >= width) text else " ".take(width-text.length) + text
}
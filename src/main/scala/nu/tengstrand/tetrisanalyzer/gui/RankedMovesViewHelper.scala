package nu.tengstrand.tetrisanalyzer.gui

import nu.tengstrand.tetrisanalyzer.move.{Move, MoveEquity}
import java.util.Locale

class RankedMovesViewHelper {
  def rowInfo(firstRow: Boolean, rankedMove: MoveEquity, maxEquity: Double): String = {
    val move = rankedMove.pieceMove.move
    val equityWidth = maxEquity.toInt.toString.length + 5
    val delta = if (firstRow) " " else "+"

    rotationAndX(move) + "  " + alignRight(delta + withThreeDecimals(rankedMove.equity), equityWidth)
  }

  def rotationAndX(move: Move) = move.rotation + "" + move.x

  def withThreeDecimals(equity: Double): String = {
    "%.3f".formatLocal(Locale.ENGLISH, equity)
  }

  def alignRight(text: String, width: Int) = if (text.length >= width) text else " ".take(width-text.length) + text
}
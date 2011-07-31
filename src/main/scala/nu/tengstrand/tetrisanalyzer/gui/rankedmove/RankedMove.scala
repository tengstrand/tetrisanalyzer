package nu.tengstrand.tetrisanalyzer.gui.rankedmove

import nu.tengstrand.tetrisanalyzer.move.{Move, MoveEquity}
import java.util.Locale

class RankedMove(val row: Int, val moveEquity: MoveEquity, maxX: Int, maxEquity: Double) extends AlignRight {
  private var showY = false
  private var maxY = 0
  private var isDuplicatedVX: Boolean = false

  private val isFirstRow = row == 1

  def asText: String = {
    val move = moveEquity.pieceMove.move
    val pieceHeight = moveEquity.pieceMove.piece.height(move.rotation)
    val equityWidth = maxEquity.toInt.toString.length + 5
    val equityDiff = if (isFirstRow) " " else "+"
    val equity = alignRight(equityDiff + withThreeDecimals(moveEquity.equity), equityWidth)

    if (showY)
      withY(move, pieceHeight, maxX, maxY, isDuplicatedVX) + "  " + equity
    else
      withoutY(move, maxX) + "  " + equity
  }

  def withYColumn(maxY: Int, isDuplicatedVX: Boolean): RankedMove = {
    showY = true
    this.maxY = maxY
    this.isDuplicatedVX = isDuplicatedVX
    this
  }

  def withoutY(move: Move, maxX: Int) = move.rotation + " " + alignRight((move.x + 1).toString, maxX.toString.length)

  def withY(move: Move, pieceHeight: Int, maxX: Int, maxY: Int, isDuplicatedVX: Boolean) = {
    val xWidth = maxX.toString.length
    val yWidth = maxY.toString.length + 1
    val y = if (isDuplicatedVX) alignRight((maxY - move.y - pieceHeight + 1).toString, yWidth) else " " * yWidth
    move.rotation + " " + alignRight((move.x + 1).toString, xWidth) + y
  }

  def withThreeDecimals(equity: Double): String = {
    "%.3f".formatLocal(Locale.ENGLISH, equity)
  }

  override def toString = asText
}

package com.github.tetrisanalyzer.board

object BoardOutline {
  def apply(board: Board) = {
    val outline = Array.ofDim[Int](board.width + 1)
    var minY = Int.MaxValue

    for (x <- 0 until board.width) {
      var y = 0
      while (board.isFree(x,y))
        y += 1
      outline(x) = y

      if (y < minY)
        minY = y
    }
    outline(board.width) = 0

    new BoardOutline(outline, minY)
  }
}

/**
 * Helper for classes that evaluates the outline.
 */
class BoardOutline(val outline: Array[Int], val minY: Int) {
  def get(x: Int) = { outline(x) }

  override def equals(that: Any) = that match {
    case other: BoardOutline => outline.toList == other.outline.toList
    case _ => false
  }

  override def toString = outline.toList.toString
}
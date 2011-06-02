package com.github.tetrisanalyzer.board

object Board {
  val EmptyLine = 0

  /**
   * Creates an empty board.
   */
  def apply(width: Int = 10, height: Int = 20) = {
    val lines = Array.fill(height) { EmptyLine }
    new Board(width, height, lines)
  }

  /**
   * Create a board from a string representation.
   */
  def apply(lines: Array[String]) = {
    val width = lines(0).length - 2
    val height = lines.length - 1

    require(lines(height) == bottomTextLine(width))

    val boardLines = Array.tabulate(height) (
      ((y) => fromText(width, lines(y)))
    )
    new Board(width, height, boardLines)
  }

  def bottomTextLine(width: Int) = "#" * (width + 2)

  private def fromText(width: Int, textLine: String): Int = {
    var line = EmptyLine
    for (i <- width to 1 by -1) {
      line <<= 1
      line |= (if (textLine(i) == '-') 0 else 1)
    }
    line
  }
}

/**
 * Represents a Tetris board. Default size is 10x20.
 *
 * Each line is represented by a 32 bit integer where each bit corresponds to a column on the board.
 * Bit 0 corresponds to the x-value 0 (left most position), bit 1 to x-value 1 etc.
 *
 * This is a highly optimized version that does not follow best practice in object-orientation!
 */
class Board(val width: Int, val height: Int, val lines: Array[Int]) {
  private val completeLine = calculateCompleteLine(width)

  require(width >= 4 && width <= 32)
  require(height >= 4)

  def worstBoard: Board = {
    val board = Board(width, height)
    val worstLines = Array.tabulate(height) (
      ((y) => worstLine(width, y % 2 == 0))
    )
    new Board(width, height, worstLines)
  }

  private def worstLine(width: Int, even: Boolean): Int = {
    var line = Board.EmptyLine

    for (x <- 1 to width) {
      line <<= 1
      if (x % 2 == 1)
        line |= 1
    }
    if (even)
      line
    else
      line >> 1
  }

  private def calculateCompleteLine(width: Int): Int = {
    var line = Board.EmptyLine

    for (x <- 1 to width) {
      line <<= 1
      line |= 1
    }
    line
  }

  /**
   * True if the specified dot is not occupied.
   */
  def isFree(x: Int, y: Int) = {
    try {
      (lines(y) & (1 << x)) == 0
    } catch {
      case e: IndexOutOfBoundsException => false
    }
  }

  /**
   * Clears completed lines and returns number of cleared lines.
   * This method is called after a piece has been placed on the board.
   *   pieceY: the y position of the piece.
   *   pieceHeight: height of the piece.
   */
  def clearLines(pieceY: Int, pieceHeight: Int): Int = {
    var clearedLines = 0
    var y1 = pieceY + pieceHeight

    // Find first line to clear
    do {
      y1 -= 1
      if (lines(y1) == completeLine)
        clearedLines += 1
    } while (clearedLines == 0 && y1 > pieceY)

    // Clear lines
    if (clearedLines > 0) {
      var y2 = y1

      while (y1 >= 0) {
        y2 -= 1
        while (y2 >= pieceY && lines(y2) == completeLine) {
          clearedLines += 1
          y2 -= 1
        }
        if (y2 >= 0)
          lines(y1) = lines(y2)
        else
          lines(y1) = Board.EmptyLine

        y1 -= 1
      }
    }
    clearedLines
  }

  /**
   * Returns a new copy of this (mutable) board.
   */
  def copy: Board = {
    val copyLines = new Array[Int](height)
    lines.copyToArray(copyLines)
    new Board(width, height, copyLines)
  }

  /**
   * Restores this (mutable) bard from a another board.
   */
  def restore(other: Board) {
    other.lines.copyToArray(lines)
  }

  override def equals(that: Any) = that match {
    case other: Board => lines.toList == other.lines.toList
    case _ => false
  }

  override def toString: String = {
    lines.map(boardLineAsString(_)).mkString("\n") + "\n" + Board.bottomTextLine(width)
  }

  /**
   * Converts a board line into its string representation.
   */
  private def boardLineAsString(boardLine: Int): String = {
    var result = "#"

    for (i <- 0 until width)
      result += (if (((boardLine >> i) & 1) == 0) "-" else "x")

    result + "#"
  }
}

package nu.tengstrand.tetrisanalyzer.board

object Board {
  val EmptyRow = 0

  /**
   * Creates an empty board.
   */
  def apply(width: Int = 10, height: Int = 20) = {
    val rows = Array.fill(height) { EmptyRow }
    new Board(width, height, rows)
  }

  /**
   * Create a board from a string representation.
   */
  def apply(rows: Array[String]) = {
    val width = rows(0).length - 2
    val height = rows.length - 1

    require(rows(height) == bottomTextRow(width))

    val boardRows = Array.tabulate(height) (
      ((y) => fromText(width, rows(y)))
    )
    new Board(width, height, boardRows)
  }

  def bottomTextRow(width: Int) = "#" * (width + 2)

  private def fromText(width: Int, textRow: String): Int = {
    var row = EmptyRow
    for (i <- width to 1 by -1) {
      row <<= 1
      row |= (if (textRow(i) == '-') 0 else 1)
    }
    row
  }
}

/**
 * Represents a Tetris board. Default size is 10x20.
 *
 * Each row is represented by a 32 bit integer where each bit corresponds to a column on the board.
 * Bit 0 corresponds to the x-value 0 (left most position), bit 1 to x-value 1 etc.
 *
 * This is a highly optimized version that does not follow best practice in object-orientation!
 */
class Board(val width: Int, val height: Int, val rows: Array[Int]) {
  private val completeRow = calculateCompleteRow(width)

  require(width >= 4 && width <= 32)
  require(height >= 4)

  /**
   * Returns a "junk board" that is used by the BoardEvaluator to calculate the "max equity" for
   * a given board size to compensate if any of the next seven possible pieces are occupied.
   * Example:
   *
   * #-x-x-x-x-x#
   * #x-x-x-x-x-#
   * #-x-x-x-x-x#
   * #x-x-x-x-x-#
   * #-x-x-x-x-x#
   * #x-x-x-x-x-#
   * ############
   */
  def junkBoard: Board = {
    val board = Board(width, height)
    val junkRows = Array.tabulate(height) (
      ((y) => junkRow(width, y % 2 == 0))
    )
    new Board(width, height, junkRows)
  }

  private def junkRow(width: Int, even: Boolean): Int = {
    var row = Board.EmptyRow

    for (x <- 1 to width) {
      row <<= 1
      if (x % 2 == 1)
        row |= 1
    }
    if (even)
      row
    else
      row >> 1
  }

  private def calculateCompleteRow(width: Int): Int = {
    var row = Board.EmptyRow

    for (x <- 1 to width) {
      row <<= 1
      row |= 1
    }
    row
  }

  /**
   * True if the specified dot is not occupied.
   */
  def isFree(x: Int, y: Int) = {
    try {
      (rows(y) & (1 << x)) == 0
    } catch {
      case e: IndexOutOfBoundsException => false
    }
  }

  /**
   * Clears completed rows and returns number of cleared rows.
   * This method is called after a piece has been placed on the board.
   *   pieceY: the y position of the piece.
   *   pieceHeight: height of the piece.
   */
  def clearRows(pieceY: Int, pieceHeight: Int): Int = {
    var clearedRows = 0
    var y1 = pieceY + pieceHeight

    // Find first row to clear
    while (clearedRows == 0 && y1 > pieceY) {
      y1 -= 1
      if (rows(y1) == completeRow)
        clearedRows += 1
    }

    // Clear rows
    if (clearedRows > 0) {
      var y2 = y1

      while (y1 >= 0) {
        y2 -= 1
        while (y2 >= pieceY && rows(y2) == completeRow) {
          clearedRows += 1
          y2 -= 1
        }
        if (y2 >= 0)
          rows(y1) = rows(y2)
        else
          rows(y1) = Board.EmptyRow

        y1 -= 1
      }
    }
    clearedRows
  }

  /**
   * Returns a new copy of this (mutable) board.
   */
  def copy: Board = {
    val copyRows = new Array[Int](height)
    rows.copyToArray(copyRows)
    new Board(width, height, copyRows)
  }

  /**
   * Restores this (mutable) bard from a another board.
   */
  def restore(other: Board): Unit = {
    other.rows.copyToArray(rows)
  }

  override def equals(that: Any) = that match {
    case other: Board => rows.toList == other.rows.toList
    case _ => false
  }

  override def toString: String = {
    rows.map(boardRowAsString(_)).mkString("\n") + "\n" + Board.bottomTextRow(width)
  }

  /**
   * Converts a board row into its string representation.
   */
  private def boardRowAsString(boardRow: Int): String = {
    var result = "#"

    for (i <- 0 until width)
      result += (if (((boardRow >> i) & 1) == 0) "-" else "x")

    result + "#"
  }
}

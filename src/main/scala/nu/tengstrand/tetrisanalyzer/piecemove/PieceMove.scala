package nu.tengstrand.tetrisanalyzer.piecemove

import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.move.Move
import collection.immutable.{Set, HashSet}

object PieceMove {
  val AllBitsCleared = 0

  def apply(board: Board, piece: Piece, move: Move): PieceMove = {
    val AllBitsSet = -1

    val pieceHeight = piece.height(move.rotation)
    val orLines = Array.fill(pieceHeight) { AllBitsCleared }
    val andLines = Array.fill(pieceHeight) { AllBitsSet }
    val boardLineIndices = Array.ofDim[Int](pieceHeight)

    for (y <- 0 until pieceHeight)
      boardLineIndices(y) = move.y + y

    piece.shape(move.rotation).dots.foreach(point => orLines(point.y) |= (1 << (move.x + point.x)))
    piece.shape(move.rotation).dots.foreach(point => andLines(point.y) &= ~(1 << (move.x + point.x)))

    new PieceMove(board, piece, move, boardLineIndices, orLines, andLines)
  }
}

/**
 * Has responsible of setting and clearing a piece on the board and can tell which
 * moves are valid on the (none empty) board.
 */
class PieceMove(val board: Board, val piece: Piece, val move: Move, boardLineIndices: Array[Int],
                orLines: Array[Int], andLines: Array[Int]) {
  var down: PieceMove = null
  var asideAndRotate: Set[PieceMove] = new HashSet[PieceMove]
  private val pieceHeight = piece.height(move.rotation)

  /**
   * Sets a piece on the board, return number of cleared lines.
   */
  def setPiece(): Int = {
    for (y <- 0 until pieceHeight) {
      board.lines(boardLineIndices(y)) |= orLines(y)
    }
    board.clearLines(move.y, pieceHeight)
  }

  /**
   * Removes a piece from the board.
   */
  def clearPiece() {
    for (y <- 0 until pieceHeight) {
      board.lines(boardLineIndices(y)) &= andLines(y)
    }
  }

  /**
   * True if the piece position is not occupied.
   */
  def isFree: Boolean = {
    var y = 0
    while (y < pieceHeight && ((board.lines(boardLineIndices(y)) & orLines(y)) == PieceMove.AllBitsCleared)) {
      y += 1
    }
    y == pieceHeight
  }

  /**
   * True if this piece hasn't reached the bottom (down == null) and the move down
   * (current position where y is increased by one) is not occupied with "dots" (rest of pieces).
   */
  def canMoveDown: Boolean = {
    down != null && down.isFree
  }

  def freeAsideAndRotateMoves = {
    asideAndRotate.filter(_.isFree)
  }

  override def equals(that: Any) = that match {
    case other: PieceMove => piece == other.piece && move == other.move
    case _ => false
  }

  override def hashCode = piece.hashCode * 31 + move.hashCode

  override def toString = piece + ", " + move
}
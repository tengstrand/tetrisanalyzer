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
    val orRows = Array.fill(pieceHeight) { AllBitsCleared }
    val andRows = Array.fill(pieceHeight) { AllBitsSet }
    val boardRowIndices = Array.ofDim[Int](pieceHeight)

    for (y <- 0 until pieceHeight)
      boardRowIndices(y) = move.y + y

    piece.shape(move.rotation).dots.foreach(point => orRows(point.y) |= (1 << (move.x + point.x)))
    piece.shape(move.rotation).dots.foreach(point => andRows(point.y) &= ~(1 << (move.x + point.x)))

    new PieceMove(board, piece, move, boardRowIndices, orRows, andRows)
  }
}

/**
 * Has responsible of setting and clearing a piece on the board and can tell which
 * moves are valid on the (none empty) board.
 */
class PieceMove(val board: Board, val piece: Piece, val move: Move, boardRowIndices: Array[Int],
                orRows: Array[Int], andRows: Array[Int]) extends Ordered[PieceMove] {
  var down: PieceMove = null
  var asideAndRotate: Set[PieceMove] = new HashSet[PieceMove]
  private val pieceHeight = piece.height(move.rotation)

  var animatedPath: PieceMove = null
  private var animatedPathValue: Int = Integer.MAX_VALUE

  /**
   * Sets a piece on the board, return number of cleared rows.
   */
  def setPiece(): Int = {
    for (y <- 0 until pieceHeight) {
      board.rows(boardRowIndices(y)) |= orRows(y)
    }
    board.clearRows(move.y, pieceHeight)
  }

  /**
   * Removes a piece from the board.
   */
  def clearPiece() {
    for (y <- 0 until pieceHeight) {
      board.rows(boardRowIndices(y)) &= andRows(y)
    }
  }

  /**
   * True if the piece position is not occupied.
   */
  def isFree: Boolean = {
    var y = 0
    while (y < pieceHeight && ((board.rows(boardRowIndices(y)) & orRows(y)) == PieceMove.AllBitsCleared)) {
      y += 1
    }
    y == pieceHeight
  }

  /**
   * True if this piece hasn't reached the bottom (down == null) and the move down
   * (current position where y is increased by one) is not occupied with "dots" (pieces parts).
   */
  def canMoveDown: Boolean = {
    down != null && down.isFree
  }

  def freeAsideAndRotateMoves = {
    asideAndRotate.filter(_.isFree)
  }

  /**
   * Resets the path values so the animated path can be calculated by calling calculateAnimatedPath.
   */
  def prepareAnimatedPath() {
    if (animatedPathValue != Integer.MAX_VALUE) {
      animatedPathValue = Integer.MAX_VALUE
      asideAndRotate.foreach(_.prepareAnimatedPath())
      if (down != null)
        down.prepareAnimatedPath()
    }
  }

  /**
   * Calculates an animated path which goes backwards, linked via the reference
   * calculateAnimatedPath, from the move "at the bottom" up to the starting position.
   *
   * Make sure the method prepareAnimatedPath has been called before calling this method.
   */
  def calculateAnimatedPath(fromPieceMove: PieceMove, pathValue: Int, asideValue: Int) {
    if (pathValue + asideValue < animatedPathValue && isFree) {
      animatedPath = fromPieceMove
      animatedPathValue = pathValue + asideValue
      asideAndRotate.foreach(pieceMove => pieceMove.calculateAnimatedPath(this, animatedPathValue, asideValue + 1))
      if (canMoveDown) {
        down.calculateAnimatedPath(this, animatedPathValue, 0)
      }
    }
  }

  def compare(that: PieceMove) = {
    if (move.rotation.compare(that.move.rotation) != 0)
      move.rotation.compare(that.move.rotation)
    else if (move.x.compare(that.move.x) != 0)
      move.x.compare(that.move.x)
    else
      move.y.compare(that.move.y)
  }

  override def equals(that: Any) = that match {
    case other: PieceMove => piece == other.piece && move == other.move
    case _ => false
  }

  override def hashCode = piece.hashCode * 31 + move.hashCode

  override def toString = piece + ", " + move
}
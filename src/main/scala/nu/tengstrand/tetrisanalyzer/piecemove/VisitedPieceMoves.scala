package nu.tengstrand.tetrisanalyzer.piecemove

import collection.mutable.HashMap
import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.move.Move

/**
 * Helper class to calculate the valid moves on an empty board.
 */
class VisitedPieceMoves(board: Board, piece: Piece) {
  private val moves = Array.fill(board.height, board.width, Direction.NumberOfDirections) { 0 }
  val validMoves = new HashMap[Move, PieceMove]

  /**
   * Returns an existing piece move or creates a new.
   */
  def get(move: Move): PieceMove = validMoves.getOrElse(move, PieceMove(board, piece, move))

  /**
   * Marks a move as visited. The attribute 'movement.direction' describes the way in which the
   * movement to this location has occurred. We also add the direction 'Rotate' because
   * we don't want to rotate the piece back to current position.
   */
  def visit(movement: Movement) {
    val move = movement.pieceMove.move
    moves(move.y)(move.x)(movement.direction.index) |= (1 << move.rotation)
    moves(move.y)(move.x)(Direction.Rotate.index) |= (1 << move.rotation)
    validMoves += move -> movement.pieceMove
  }

  /**
   * True if this movement has not been visited.
   */
  def isUnvisited(movement: Movement): Boolean = {
    try {
      val move = movement.pieceMove.move
      (moves(move.y)(move.x)(movement.direction.index) & (1 << move.rotation)) == 0
    } catch {
      case e: IndexOutOfBoundsException =>
        false
    }
  }
}

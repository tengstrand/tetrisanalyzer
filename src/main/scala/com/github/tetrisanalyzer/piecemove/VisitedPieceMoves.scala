package com.github.tetrisanalyzer.piecemove

import collection.mutable.HashMap
import com.github.tetrisanalyzer.piece.Piece
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.move.Move

/**
 * Helper class for the calculation of valid moves on an empty board.
 */
class VisitedPieceMoves(board: Board, piece: Piece) {
  private val moves = Array.fill(board.height, board.width, Direction.NumberOfDirections) { 0 }
  val validMoves = new HashMap[Move, PieceMove]

  /**
   * Returns an existing piece move or creates a new.
   */
  def get(move: Move): PieceMove = validMoves.getOrElse(move, PieceMove(board, piece, move))

  /**
   * Marks a move as visited. The attribute 'movement.direction' says the manner in which the
   * transfer to this location has occurred. We also add the direction 'Rotate' because
   * we don't need to rotate the piece a full circle (to the position we just have).
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

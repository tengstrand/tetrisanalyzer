package com.github.tetrisanalyzer.move

import com.github.tetrisanalyzer.piecemove.PieceMove
import com.github.tetrisanalyzer.board.Board

object ValidMoves {
  def apply(board: Board) = {
    new ValidMoves(board)
  }
}

class ValidMoves(board: Board) {
  private var validMoves = List.empty[PieceMove]
  private val visitedMoves = Array.fill(board.height, board.width) { 0 }

  private def markAsVisited(move: Move) { visitedMoves(move.y)(move.x) |= 1 << move.rotation }
  private def isUnvisited(move: Move): Boolean = {
    try {
      (visitedMoves(move.y)(move.x) & (1 << move.rotation)) == 0
    } catch {
      case e: IndexOutOfBoundsException => false
    }
  }

  /**
   * The incoming pieceMove is at the starting position of the board, and
   * links to all possible moves on an empty board. Because the board is probably not empty,
   * every every possible move needs to be checked (and returned by this method).
   */
  def pieceMoves(pieceMove: PieceMove): List[PieceMove] = {
    if (isUnvisited(pieceMove.move)) {
      markAsVisited(pieceMove.move)

      pieceMove.freeAsideAndRotateMoves.foreach(move => pieceMoves(move))

      if (pieceMove.canMoveDown)
        pieceMoves(pieceMove.down)
      else
        validMoves = pieceMove :: validMoves
    }
    validMoves
  }
}
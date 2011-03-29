package com.github.tetrisanalyzer.piecemove

import org.junit.{Before, Test}
import com.github.tetrisanalyzer.BaseTest
import com.github.tetrisanalyzer.move.Move
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.piece.{Piece, PieceS}

class VisitedMovesTest extends BaseTest {
  private var board: Board = _
  private var piece: Piece = _

  @Before def setUp {
    board = Board(6,6)
    piece = new PieceS
  }

  @Test def visitLeft {
    val move = new Move(0,0,0)
    val visitedMoves = new VisitedPieceMoves(board, piece)
    visitedMoves.visit(getMovement(move, new Left))

    visitedMoves.isUnvisited(getMovement(move, new Left)) should be (false)
    visitedMoves.isUnvisited(getMovement(move, new Right)) should be (true)
    visitedMoves.isUnvisited(getMovement(move, new Down)) should be (true)
    visitedMoves.isUnvisited(getMovement(move, new Rotate)) should be (false)
  }

  private def getMovement(move: Move, direction: Direction) = {
    val pieceMove = PieceMove(board, piece, move)
    new Movement(pieceMove, direction)
  }
}
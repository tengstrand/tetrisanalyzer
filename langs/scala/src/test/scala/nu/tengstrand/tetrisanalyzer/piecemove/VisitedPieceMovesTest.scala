package nu.tengstrand.tetrisanalyzer.piecemove

import org.junit.{Before, Test}
import nu.tengstrand.tetrisanalyzer.BaseTest
import nu.tengstrand.tetrisanalyzer.move.Move
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piece.{Piece, PieceS}

class VisitedPieceMovesTest extends BaseTest {
  private var board: Board = Board(6,6)
  private var piece: Piece = PieceS()

  @Test def visitLeftPieceS(): Unit = {
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
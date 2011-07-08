package nu.tengstrand.tetrisanalyzer.gui.rankedmove

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.piece.PieceS
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piecemove.PieceMove
import nu.tengstrand.tetrisanalyzer.move.{Move, MoveEquity}

class RankedMovesTest extends BaseTest {

  @Test def headerX() {
    val rankedMoves = new RankedMoves(List.empty[MoveEquity]) {
      override def maxX = 9
      override def maxY = 20
      override def hasDuplicatedVX = false
    }
    rankedMoves.headerAsText should be ("    v x  Depth 0")
  }

  @Test def headerX_adjustRight() {
    val rankedMoves = new RankedMoves(List.empty[MoveEquity]) {
      override def maxX = 10
      override def maxY = 20
      override def hasDuplicatedVX = false
    }
    rankedMoves.headerAsText should be ("    v  x  Depth 0")
  }

  @Test def headerXY() {
    val rankedMoves = new RankedMoves(List.empty[MoveEquity]) {
      override def maxX = 9
      override def maxY = 20
      override def hasDuplicatedVX = true
    }
    rankedMoves.headerAsText should be ("    v x  y  Depth 0")
  }

  @Test def calculateRankedMoves() {
    val rankedMoves = new RankedMoves(equityMoves)
    rankedMoves.moves.map(_.asText).mkString("\n") should be (
      "0 2 15   1.001\n" +
      "0 3     +1.002\n" +
      "1 5     +1.003\n" +
      "0 2 18  +1.004\n" +
      "1 4     +1.005"
    )
  }

  private def equityMoves = List(
      moveEquity(0,1, 15, 1.001, 10, 5.55),
      moveEquity(0,2, 18, 1.002, 10, 5.55),
      moveEquity(1,4, 15, 1.003, 10, 5.55),
      moveEquity(0,1, 18, 1.004, 10, 5.55),
      moveEquity(1,3, 15, 1.005, 10, 5.55)
    )


  private def moveEquity(v: Int, x: Int, y: Int, equity: Double, maxX: Int, maxEquity: Double) = {
    val board = Board()
    val piece = new PieceS
    MoveEquity(PieceMove(board, piece, Move(v,x, y)), equity)
  }
}
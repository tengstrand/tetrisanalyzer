package nu.tengstrand.tetrisanalyzer.gui.rankedmove

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.piece.PieceS
import nu.tengstrand.tetrisanalyzer.piecemove.PieceMove
import nu.tengstrand.tetrisanalyzer.move.{MoveEquity, Move}
import nu.tengstrand.tetrisanalyzer.board.Board

class RankedMoveTest extends BaseTest {

  private val anyRankedMove = getRankedMove(0, 1, 18, 10.234, 10, 9)

  @Test def withoutY(): Unit = {
    anyRankedMove.withoutY(Move(1, 2, 15), 9) should be ("1 3")
  }

  @Test def withoutY_adjustRight(): Unit = {
    anyRankedMove.withoutY(Move(1, 2, 15), 10) should be ("1  3")
  }

  @Test def withY(): Unit = {
    anyRankedMove.withY(Move(1, 2, 15), 3, 9, 100, true) should be ("1 3  83")
  }

  @Test def withThreeDecimals(): Unit = {
    anyRankedMove.withThreeDecimals(12.34) should be ("12.340")
  }

  @Test def withThreeDecimals_zero(): Unit = {
    anyRankedMove.withThreeDecimals(0) should be ("0.000")
  }

  @Test def alignRight(): Unit = {
    anyRankedMove.alignRight("1.234", 7) should be ("  1.234")
  }

  @Test def alignRight_noAdjustment(): Unit = {
    anyRankedMove.alignRight("1.234", 5) should be ("1.234")
  }

  @Test def asText_maxXOneCharacter(): Unit = {
    getRankedMove(0, 1, 18, 2.504, 9, 9).asText should be ("0 2  +2.504")
  }

  @Test def asText_maxXTwoCharacters(): Unit = {
    getRankedMove(0, 1, 18, 2.504, 9, 10).asText should be ("0 2   +2.504")
  }

  @Test def asText_withYColumn(): Unit = {
    getRankedMove(0, 1, 18, 2.504, 9, 9).withYColumn(100, false).asText should be ("0 2      +2.504")
  }

  @Test def astText_withYColumn_duplicatedVX(): Unit = {
    getRankedMove(0, 1, 18, 2.504, 9, 9).withYColumn(100, true).asText should be ("0 2  81  +2.504")
  }

  private def getRankedMove(v: Int, x: Int, y: Int, equity: Double, maxX: Int, maxEquity: Double) = {
    val board = Board()
    val piece = PieceS()
    val moveEquity = MoveEquity(PieceMove(board, piece, Move(v,x, y)), equity)
    new RankedMove(2, moveEquity, maxX, maxEquity)
  }
}
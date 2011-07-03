package nu.tengstrand.tetrisanalyzer.gui

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piece.PieceS
import nu.tengstrand.tetrisanalyzer.move.{MoveEquity, Move}
import nu.tengstrand.tetrisanalyzer.piecemove.PieceMove

class RankedMovesViewHelperTest extends BaseTest {
  private val rankedMovesViewHelper = new RankedMovesViewHelper

  @Test def rotationAndX {
    rankedMovesViewHelper.rotationAndX(Move(1, 2, 3)) should be ("12")
  }

  @Test def withThreeDecimals {
    rankedMovesViewHelper.withThreeDecimals(12.34) should be ("12.340")
  }

  @Test def withThreeDecimals_zero {
    rankedMovesViewHelper.withThreeDecimals(0) should be ("0.000")
  }

  @Test def rowInfo {
    val rankedMove = getRankedMove(0, 1, 2.504, 9)
    rankedMovesViewHelper.rowInfo(false, rankedMove, 9) should be ("01  +2.504")
  }

  @Test def rowInfo_adjustRight {
    val rankedMove = getRankedMove(0, 1, 2.504, 10)
    rankedMovesViewHelper.rowInfo(false, rankedMove, 10) should be ("01   +2.504")
  }

  private def getRankedMove(v: Int, x: Int, equity: Double, maxEquity: Double) = {
    val board = Board()
    val piece = new PieceS
    MoveEquity(PieceMove(board, piece, Move(v,x, 18)), equity)
  }

  @Test def alignRight {
    rankedMovesViewHelper.alignRight("1.234", 6) should be (" 1.234")
  }

  @Test def alignRight_noAdjustment {
    rankedMovesViewHelper.alignRight("1.234", 5) should be ("1.234")
  }
}
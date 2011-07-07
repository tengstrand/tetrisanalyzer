package nu.tengstrand.tetrisanalyzer.gui.rankedmove

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test

class RankedMovesTest extends BaseTest {

  @Test def headerX {
    val rankedMoves = new RankedMoves(null) {
      override def maxX = 9
      override def maxY = 20
      override def hasDuplicatedVX = false
    }
    rankedMoves.headerAsText should be ("    v x  Depth 0")
  }

  @Test def headerX_adjustRight {
    val rankedMoves = new RankedMoves(null) {
      override def maxX = 10
      override def maxY = 20
      override def hasDuplicatedVX = false
    }
    rankedMoves.headerAsText should be ("    v  x  Depth 0")
  }

  @Test def headerXY {
    val rankedMoves = new RankedMoves(null) {
      override def maxX = 9
      override def maxY = 20
      override def hasDuplicatedVX = true
    }
    rankedMoves.headerAsText should be ("    v x  y  Depth 0")
  }
}
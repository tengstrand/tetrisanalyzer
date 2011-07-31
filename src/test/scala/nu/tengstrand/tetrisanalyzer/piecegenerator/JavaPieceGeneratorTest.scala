package nu.tengstrand.tetrisanalyzer.piecegenerator

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test

class JavaPieceGeneratorTest extends BaseTest {
  @Test def nextPiece {
    val pieceGenerator = new JavaPieceGenerator

    for (i <- 1 to 1000) {
      val pieceNumber = pieceGenerator.piece.number.toInt
      pieceNumber should be >= 1
      pieceNumber should be <= 7
    }
  }
}
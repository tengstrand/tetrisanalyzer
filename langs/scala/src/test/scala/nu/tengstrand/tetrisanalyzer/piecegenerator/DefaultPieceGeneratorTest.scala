package nu.tengstrand.tetrisanalyzer.piecegenerator

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test

class DefaultPieceGeneratorTest extends BaseTest {

  @Test def nextPiece(): Unit = {
    val pieceGenerator = new DefaultPieceGenerator(5)
    val pieces = (1 to 20).foldLeft("") { (result,n) => result + pieceGenerator.nextPiece().toString }

    pieces should be ("OLIZTTSZTTZIJTJJOLJO")
  }
}

package nu.tengstrand.tetrisanalyzer.game.startpiece

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.piecegenerator.PredictablePieceGenerator
import nu.tengstrand.tetrisanalyzer.piece._

class StartPieceGeneratorTest extends BaseTest {
  private var startPieceGenerator = new StartPieceGenerator(new PredictablePieceGenerator(List(new PieceT, new PieceL, new PieceI)))

  @Test def onePiece() {
    startPieceGenerator.piece(false) should be (new StartPiece(new PieceT))
    startPieceGenerator.piece(false) should be (new StartPiece(new PieceT))
  }

  @Test def twoPieces() {
    startPieceGenerator.piece(true) should be (new StartPiece(new PieceT, new PieceL))
    startPieceGenerator.piece(true) should be (new StartPiece(new PieceT, new PieceL))
  }

  @Test def nextPiece() {
    val startPiece = startPieceGenerator.nextPiece(false)
    startPiece.nextPiece() should be (new PieceL)
    startPiece.hasNext should be (false)
  }

  @Test def nextPieces() {
    val startPiece = startPieceGenerator.nextPiece(true)
    startPiece.nextPiece() should be (new PieceI)
    startPiece.nextPiece() should be (new PieceO)
    startPiece.hasNext should be (false)
  }
}

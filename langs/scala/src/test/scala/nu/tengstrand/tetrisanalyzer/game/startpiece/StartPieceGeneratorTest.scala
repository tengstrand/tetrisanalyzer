package nu.tengstrand.tetrisanalyzer.game.startpiece

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.piecegenerator.PredictablePieceGenerator
import nu.tengstrand.tetrisanalyzer.piece._

class StartPieceGeneratorTest extends BaseTest {
  private var startPieceGenerator = new StartPieceGenerator(new PredictablePieceGenerator(List(PieceT(), PieceL(), PieceI())))

  @Test def onePiece(): Unit = {
    startPieceGenerator.piece(false) should be (new StartPiece(PieceT()))
    startPieceGenerator.piece(false) should be (new StartPiece(PieceT()))
  }

  @Test def twoPieces(): Unit = {
    startPieceGenerator.piece(true) should be (new StartPiece(PieceT(), Some(PieceL())))
    startPieceGenerator.piece(true) should be (new StartPiece(PieceT(), Some(PieceL())))
  }

  @Test def nextPiece(): Unit = {
    val startPiece = startPieceGenerator.nextPiece(false)
    startPiece.firstPiece should be (PieceL())
    startPiece.hasNext should be (false)
  }

  @Test def nextPieces(): Unit = {
    val startPiece = startPieceGenerator.nextPiece(true)
    startPiece.firstPiece should be (PieceL())
    startPiece.secondPiece should be (Some(PieceI()))
    startPiece.hasNext should be (true)
  }
}

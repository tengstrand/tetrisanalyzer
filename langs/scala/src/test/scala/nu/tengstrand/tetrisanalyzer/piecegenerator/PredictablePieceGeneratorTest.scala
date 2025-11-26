package nu.tengstrand.tetrisanalyzer.piecegenerator

import org.junit.Test
import nu.tengstrand.tetrisanalyzer.BaseTest
import nu.tengstrand.tetrisanalyzer.piece.{PieceO, PieceI, PieceZ, PieceS}

class PredictablePieceGeneratorTest extends BaseTest {

  @Test def piece(): Unit = {
    val pieceGenerator = new PredictablePieceGenerator(List(PieceS(), PieceZ()))

    pieceGenerator.nextPiece() should  be (PieceS())
    pieceGenerator.nextPiece() should  be (PieceZ())
  }

  @Test def pieceAndNextPiece(): Unit = {
    val pieceGenerator = new PredictablePieceGenerator(List(PieceS(), PieceZ(), PieceI()))
    val result = pieceGenerator.nextPiece() ::
      pieceGenerator.nextPiece() ::
      pieceGenerator.nextPiece() :: Nil;

    result should be (List(PieceS(), PieceZ(), PieceI()))
  }

  @Test def runOutOfPieces(): Unit = {
    val pieceGenerator = new PredictablePieceGenerator(List(PieceI()))
    val result = pieceGenerator.nextPiece() ::
      pieceGenerator.nextPiece() ::
      pieceGenerator.nextPiece() :: Nil;

    result should be (List(PieceI(), PieceO(), PieceO()))
  }
}
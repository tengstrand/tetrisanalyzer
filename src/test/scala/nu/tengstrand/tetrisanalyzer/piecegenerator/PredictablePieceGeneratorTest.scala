package nu.tengstrand.tetrisanalyzer.piecegenerator

import org.junit.Test
import nu.tengstrand.tetrisanalyzer.BaseTest
import nu.tengstrand.tetrisanalyzer.piece.{PieceO, PieceI, PieceZ, PieceS}

class PredictablePieceGeneratorTest extends BaseTest {

  @Test def piece {
    val pieceGenerator = new PredictablePieceGenerator(List(new PieceS, new PieceZ))

    pieceGenerator.piece should  be (new PieceS)
    pieceGenerator.piece should  be (new PieceS)
  }

  @Test def pieceAndNextPiece {
    val pieceGenerator = new PredictablePieceGenerator(List(new PieceS, new PieceZ, new PieceI))
    val result = pieceGenerator.piece ::
      pieceGenerator.nextPiece() ::
      pieceGenerator.nextPiece() :: Nil;

    result should be (List(new PieceS(), new PieceZ(), new PieceI()))
  }

  @Test def runOutOfPieces {
    val pieceGenerator = new PredictablePieceGenerator(List(new PieceI))
    val result = pieceGenerator.piece ::
      pieceGenerator.nextPiece() ::
      pieceGenerator.nextPiece() :: Nil;

    result should be (List(new PieceI(), new PieceO(), new PieceO()))
  }
}
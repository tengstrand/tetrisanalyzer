package com.github.tetrisanalyzer.piecegenerator

import org.junit.Test
import com.github.tetrisanalyzer.BaseTest
import com.github.tetrisanalyzer.piece.{PieceO, PieceI, PieceZ, PieceS}

class PredictablePieceGeneratorTest extends BaseTest {

  @Test def nextPieceNumber {
    val pieceGenerator = new PredictablePieceGenerator(List(new PieceS, new PieceZ, new PieceI))
    val result = pieceGenerator.nextPieceNumber ::
      pieceGenerator.nextPieceNumber ::
      pieceGenerator.nextPieceNumber :: Nil;

    result should be (List(new PieceS().number, new PieceZ().number, new PieceI().number))
  }

  @Test def runOutOfPieces {
    val pieceGenerator = new PredictablePieceGenerator(List(new PieceI))
    val result = pieceGenerator.nextPieceNumber ::
      pieceGenerator.nextPieceNumber ::
      pieceGenerator.nextPieceNumber :: Nil;

    result should be (List(new PieceI().number, new PieceO().number, new PieceO().number))
  }
}
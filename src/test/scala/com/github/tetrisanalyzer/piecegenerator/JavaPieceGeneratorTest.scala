package com.github.tetrisanalyzer.piecegenerator

import com.github.tetrisanalyzer.BaseTest
import org.junit.Test

class JavaPieceGeneratorTest extends BaseTest {
  @Test def nextPiece() {
    val pieceGenerator = new JavaPieceGenerator

    for (i <- 1 to 1000) {
      val pieceNumber = pieceGenerator.nextPiece.number.toInt
      pieceNumber should be >= 1
      pieceNumber should be <= 7
    }
  }
}
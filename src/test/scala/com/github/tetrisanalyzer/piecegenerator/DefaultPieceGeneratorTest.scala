package com.github.tetrisanalyzer.piecegenerator

import com.github.tetrisanalyzer.BaseTest
import org.junit.Test

class DefaultPieceGeneratorTest extends BaseTest {

  @Test def nextPiece {
    val pieceGenerator = new DefaultPieceGenerator(5)
    val pieces = (1 to 20).foldLeft("") { (result,n) => result + pieceGenerator.nextPiece.toString }

    pieces should be ("OLIZTTSZTTZIJTJJOLJO")
  }
}

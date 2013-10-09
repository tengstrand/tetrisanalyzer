package nu.tengstrand.tetrisanalyzer.piecegenerator

import nu.tengstrand.tetrisanalyzer.piece.Piece
import java.util.Random

/**
 * Uses the standard random generator in Java.
 */
class JavaPieceGenerator  extends PieceGenerator {
  private val random = new Random

  def nextPieceNumber = random.nextInt(Piece.NumberOfPieceTypes) + 1
}
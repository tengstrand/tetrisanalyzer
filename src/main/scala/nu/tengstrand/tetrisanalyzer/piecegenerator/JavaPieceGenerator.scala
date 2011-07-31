package nu.tengstrand.tetrisanalyzer.piecegenerator

import nu.tengstrand.tetrisanalyzer.piece.Piece
import java.util.Random

/**
 * Uses the standard random generator in Java.
 */
class JavaPieceGenerator  extends PieceGenerator {
  private val random = new Random
  private var number = nextRandomPieceNumber

  private def nextRandomPieceNumber = random.nextInt(Piece.NumberOfPieceTypes) + 1

  def pieceNumber = number

  def prepareNext() { number = nextRandomPieceNumber }
}
package nu.tengstrand.tetrisanalyzer.piecegenerator

import nu.tengstrand.tetrisanalyzer.piece.Piece

trait PieceGenerator {
  def nextPieceNumber: Int

  def nextPiece() = {
    val number = nextPieceNumber
    require(number >= 1 && number <= 7, "Piece number must be in the range 1..7, found: " + number)
    Piece(number)
  }
}
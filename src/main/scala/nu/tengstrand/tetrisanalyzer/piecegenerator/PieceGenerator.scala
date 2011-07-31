package nu.tengstrand.tetrisanalyzer.piecegenerator

import nu.tengstrand.tetrisanalyzer.piece.Piece

trait PieceGenerator {
  def pieceNumber: Int
  def prepareNext()

  def piece = {
    val number = pieceNumber
    require(number >= 1 && number <= 7, "Piece number must be in the range 1..7, found: " + number)
    Piece(number)
  }

  def nextPiece() = {
    prepareNext()
    piece
  }
}
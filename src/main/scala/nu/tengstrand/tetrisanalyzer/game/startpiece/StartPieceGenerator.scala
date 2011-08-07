package nu.tengstrand.tetrisanalyzer.game.startpiece

import nu.tengstrand.tetrisanalyzer.piecegenerator.PieceGenerator

class StartPieceGenerator(pieceGenerator: PieceGenerator) {
  var firstPiece = pieceGenerator.nextPiece()
  var secondPiece = pieceGenerator.nextPiece()

  def piece(getTwoPieces: Boolean): StartPiece = {
    if (!getTwoPieces)
      new StartPiece(firstPiece)
    else
      new StartPiece(firstPiece, secondPiece)
  }

  def nextPiece(getTwoPieces: Boolean): StartPiece = {
    firstPiece = secondPiece
    secondPiece = pieceGenerator.nextPiece()
    piece(getTwoPieces)
  }
}
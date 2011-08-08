package nu.tengstrand.tetrisanalyzer.game.startpiece

import nu.tengstrand.tetrisanalyzer.piecegenerator.PieceGenerator

class StartPieceGenerator(pieceGenerator: PieceGenerator) {
  var firstPiece = pieceGenerator.nextPiece()
  var secondPiece = pieceGenerator.nextPiece()

  def piece(showNextPiece: Boolean): StartPiece = {
    if (!showNextPiece)
      new StartPiece(firstPiece)
    else
      new StartPiece(firstPiece, secondPiece)
  }

  def nextPiece(showNextPiece: Boolean): StartPiece = {
    firstPiece = secondPiece
    secondPiece = pieceGenerator.nextPiece()
    piece(showNextPiece)
  }
}
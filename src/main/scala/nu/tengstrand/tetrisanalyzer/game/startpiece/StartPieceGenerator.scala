package nu.tengstrand.tetrisanalyzer.game.startpiece

import nu.tengstrand.tetrisanalyzer.piecegenerator.PieceGenerator
import nu.tengstrand.tetrisanalyzer.piece.Piece

class StartPieceGenerator(var pieceGenerator: PieceGenerator) {
  var firstPiece: Piece = null
  var secondPiece: Piece = null

  initPieces()

  def setPieceGenerator(pieceGenerator: PieceGenerator) {
    this.pieceGenerator = pieceGenerator
    initPieces()
  }

  private def initPieces() {
    firstPiece = pieceGenerator.nextPiece()
    secondPiece = pieceGenerator.nextPiece()
  }

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
package nu.tengstrand.tetrisanalyzer.game.startpiece

import nu.tengstrand.tetrisanalyzer.piecegenerator.PieceGenerator
import nu.tengstrand.tetrisanalyzer.piece.Piece

class StartPieceGenerator(var pieceGenerator: PieceGenerator) {
  var firstPiece: Piece = pieceGenerator.nextPiece()
  var secondPiece: Option[Piece] = Some(pieceGenerator.nextPiece())

  def setPieceGenerator(pieceGenerator: PieceGenerator): Unit = {
    this.pieceGenerator = pieceGenerator
    initPieces()
  }

  private def initPieces(): Unit = {
    firstPiece = pieceGenerator.nextPiece()
    secondPiece = Some(pieceGenerator.nextPiece())
  }

  def piece(showNextPiece: Boolean): StartPiece = {
    if (!showNextPiece)
      new StartPiece(firstPiece)
    else
      new StartPiece(firstPiece, secondPiece)
  }

  def nextPiece(showNextPiece: Boolean): StartPiece = {
    firstPiece = secondPiece.getOrElse(throw new IllegalStateException("No second piece available"))
    secondPiece = Some(pieceGenerator.nextPiece())
    piece(showNextPiece)
  }
}
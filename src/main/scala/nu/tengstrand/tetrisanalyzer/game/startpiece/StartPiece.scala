package nu.tengstrand.tetrisanalyzer.game.startpiece

import nu.tengstrand.tetrisanalyzer.piece.Piece

case class StartPiece(var firstPiece: Piece, var secondPiece: Piece = null) {

  def hasNext = secondPiece != null

  def nextPiece(): Piece = {
    val piece = firstPiece
    firstPiece = secondPiece
    secondPiece = null
    piece
  }
}
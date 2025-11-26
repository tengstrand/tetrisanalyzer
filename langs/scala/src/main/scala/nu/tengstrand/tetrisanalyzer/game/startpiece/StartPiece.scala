package nu.tengstrand.tetrisanalyzer.game.startpiece

import nu.tengstrand.tetrisanalyzer.piece.Piece

case class StartPiece(var firstPiece: Piece, var secondPiece: Option[Piece] = None) {

  def hasNext = secondPiece.isDefined

  def nextPiece(): Piece = {
    val piece = firstPiece
    secondPiece match {
      case Some(next) =>
        firstPiece = next
        secondPiece = None
      case None =>
        ()
    }
    piece
  }
}
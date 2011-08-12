package nu.tengstrand.tetrisanalyzer.piece

object PieceAny {
  val Number = 8.toByte

  def apply() = new PieceAny
}

class PieceAny extends Piece {
  val number = PieceAny.Number
  val character = 'x'
  protected val widths = Array.empty[Int]
  protected val heights = Array.empty[Int]
  protected val shapes = Array.empty[PieceShape]
}

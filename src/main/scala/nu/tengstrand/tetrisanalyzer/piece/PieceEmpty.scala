package nu.tengstrand.tetrisanalyzer.piece

object PieceEmpty {
  val Number = 0.toByte

  def apply() = new PieceEmpty
}

class PieceEmpty extends Piece {
  val number = PieceEmpty.Number
  val character = '-'
  protected val widths = Array.empty[Int]
  protected val heights = Array.empty[Int]
  protected val shapes = Array.empty[PieceShape]
}
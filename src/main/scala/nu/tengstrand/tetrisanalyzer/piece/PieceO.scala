package nu.tengstrand.tetrisanalyzer.piece

object PieceO {
  def apply() = new PieceO
}

class PieceO extends Piece {
  val number = 7.toByte
  val character = 'O'
  protected val widths = Array(2)
  protected val heights = Array(2)
  protected val shapes = Array(
    new PieceShape(Array(Point(0,0), Point(1,0), Point(0,1), Point(1,1)))
  )
}

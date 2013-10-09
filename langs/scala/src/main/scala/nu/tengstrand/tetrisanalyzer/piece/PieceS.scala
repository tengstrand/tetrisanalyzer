package nu.tengstrand.tetrisanalyzer.piece

object PieceS {
  def apply() = new PieceS
}

class PieceS extends Piece {
  val number = 3.toByte
  val character = 'S'
  protected val widths = Array(3, 2)
  protected val heights = Array(2, 3)
  protected val shapes = Array(
    new PieceShape(Array(Point(1,0), Point(2,0), Point(0,1), Point(1,1))),
    new PieceShape(Array(Point(0,0), Point(0,1), Point(1,1), Point(1,2)))
  )
}

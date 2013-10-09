package nu.tengstrand.tetrisanalyzer.piece

object PieceZ {
  def apply() = new PieceZ
}

class PieceZ extends Piece {
  val number = 2.toByte
  val character = 'Z'
  protected val widths = Array(3, 2)
  protected val heights = Array(2, 3)
  protected val shapes = Array(
    new PieceShape(Array(Point(0,0), Point(1,0), Point(1,1), Point(2,1))),
    new PieceShape(Array(Point(1,0), Point(0,1), Point(1,1), Point(0,2)))
  )
}

package nu.tengstrand.tetrisanalyzer.piece

class PieceI extends Piece {
  val number = 1.toByte
  val character = 'I'
  protected val widths = Array(4, 1)
  protected val heights = Array(1, 4)
  protected val shapes = Array(
    new PieceShape(Array(Point(0,0), Point(1,0), Point(2,0), Point(3,0))),
    new PieceShape(Array(Point(0,0), Point(0,1), Point(0,2), Point(0,3)))
  )
}
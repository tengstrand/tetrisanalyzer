package com.github.tetrisanalyzer.piece

object PieceAny {
  val Number = 8.toByte
}

class PieceAny extends Piece {
  val number = PieceAny.Number
  val character = 'x'
  protected val widths = Array.empty[Int]
  protected val heights = Array.empty[Int]
  protected val shapes = Array.empty[PieceShape]
}

package com.github.tetrisanalyzer.piece

object Point {
  def apply(x: Int, y: Int) = new Point(x, y)
}

class Point(val x: Int, val y: Int) {
  override def equals(that: Any) = that match {
    case other: Point => x == other.x && y == other.y
    case _ => false
  }
}

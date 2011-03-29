package com.github.tetrisanalyzer.move

import rotation.RotationDirection

object Move {
  def apply(rotation: Int, x: Int, y: Int) = new Move(rotation, x, y)
}

/**
 * Holds a position (x,y) and rotation ("angle") for a piece on the board.
 */
class Move(val rotation: Int, val x: Int, val y: Int) {
  def up = new Move(rotation, x, y-1)
  def down = new Move(rotation, x, y + 1)
  def left = new Move(rotation, x - 1, y)
  def right = new Move(rotation, x + 1, y)
  def rotate(rotationType: RotationDirection, rotationModulus: Int) = new Move((rotation + rotationType.direction) & rotationModulus, x, y)

  override def equals(that: Any) = that match {
    case other: Move => x == other.x && y == other.y && rotation == other.rotation
    case _ => false
  }

  override def hashCode = (x + y*1000 + rotation * 10000).hashCode
  override def toString = "(" + rotation + "," + x + ", " + y + ")"
}

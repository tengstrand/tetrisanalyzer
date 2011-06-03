package nu.tengstrand.tetrisanalyzer.piecemove

object Direction {
  val NumberOfDirections = 4
  val Rotate = new Rotate
  val Down = new Down
}

/**
 * Type of piece move: Left, Right, Down and Rotate
 */
abstract class Direction {
  val index: Int

  override def equals(that: Any) = that match {
    case other: Direction => index == other.index
    case _ => false
  }
}

class Down extends Direction { val index = 0; override def toString = "Down" }
class Rotate extends Direction { val index = 1; override def toString = "Rotate" }
class Left extends Direction { val index = 2; override def toString = "Left" }
class Right extends Direction { val index = 3; override def toString = "Right" }

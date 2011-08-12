package nu.tengstrand.tetrisanalyzer.move.rotation

object RotationDirection {
  val NumberOfDirections = 2
}

abstract class RotationDirection {
  val index: Int
  val direction: Int
}
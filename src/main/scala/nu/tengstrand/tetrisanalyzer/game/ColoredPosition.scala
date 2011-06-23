package nu.tengstrand.tetrisanalyzer.game

trait ColoredPosition {
  def width: Int
  def height: Int
  def colorValue(x: Int, y: Int): Int
}
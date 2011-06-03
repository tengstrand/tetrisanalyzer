package nu.tengstrand.tetrisanalyzer.game

trait PositionModel {
  def width: Int
  def height: Int
  def colorValue(x: Int, y: Int): Int
}
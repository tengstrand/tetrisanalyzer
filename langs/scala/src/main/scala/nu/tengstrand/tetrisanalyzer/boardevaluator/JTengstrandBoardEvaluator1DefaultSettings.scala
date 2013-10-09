package nu.tengstrand.tetrisanalyzer.boardevaluator

class JTengstrandBoardEvaluator1DefaultSettings extends JTengstrandBoardEvaluator1Settings {
  val heightFactor = List[Double](7, 7, 2.5, 2.2, 1.8, 1.3, 1.0, 0.9, 0.7, 0.6, 0.5, 0.4, 0.3, 0.25, 0.2, 0.15, 0.1, 0.1, 0.1, 0.1, 0.1, 0.09)
  val blocksPerRowHollowFactor = List[Double](0, 0, 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.553)
  def areaWidthFactor = List[Double](0, 4.25, 2.39, 3.1, 2.21, 2.05, 1.87, 1.52, 1.34, 1.18, 1 )
  def areaHeightFactor = List(0, 0.5, 1.19, 2.3, 3.1, 4.6, 5.6)
  def areaHeightFactorEqualWallHeight = List(0, 0.42, 1.05, 2.2, 3.1, 4.6, 5.6)
}
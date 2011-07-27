package nu.tengstrand.tetrisanalyzer.boardevaluator

trait JTengstrandBoardEvaluator1Settings {
  def heightFactor: List[Double]
  def blocksPerRowHollowFactor: List[Double]
  def areaWidthFactor: List[Double]
  def areaHeightFactor: List[Double]
  def areaHeightFactorEqualWallHeight: List[Double]

  def calculateHeightFactor(boardHeight: Int): Array[Double] = {
    val heights = heightFactor

    if (boardHeight < heights.length)
      heights.take(boardHeight + 1).toArray
    else
      (List.fill(boardHeight - heights.length + 1) { heights.head } ::: heights).toArray
  }

  def calculateBlocksPerRowHollowFactor(boardWidth: Int): Array[Double] = {
    val tail = blocksPerRowHollowFactor

    if (boardWidth > tail.length)
      (List.fill(boardWidth - tail.length) { 0.0 } ::: tail).toArray
    else
      tail.takeRight(boardWidth).toArray
  }

  def calculateAreaWidthFactor(boardWidth: Int): Array[Double] = {
    val head = areaWidthFactor

    if (boardWidth < head.size)
      head.take(boardWidth + 1).toArray
    else {
      val factor = head.takeRight(1)(0) / head.takeRight(2)(0)
      (head ::: List.tabulate(boardWidth - head.length + 1) (((n) => powSeries(head.takeRight(1)(0), factor, n)))).toArray
    }
  }

  private def powSeries(initialValue: Double, factor: Double, n: Int) = (initialValue * scala.math.pow(factor, n + 1) * 100).round / 100.0

  def calculateAreaHeightFactor(boardHeight: Int): Array[Double] = calculateAreaHeightFactor(boardHeight, areaHeightFactor)

  def calculateAreaHeightFactorEqualWallHeight(boardHeight: Int): Array[Double] = calculateAreaHeightFactor(boardHeight, areaHeightFactorEqualWallHeight)

  private def calculateAreaHeightFactor(boardHeight: Int, params: List[Double]): Array[Double] = {
    if (boardHeight < params.length)
      params.take(boardHeight + 1).toArray
    else {
      val lastValue = params.takeRight(1)(0)
      val secondLastValue = params.takeRight(2)(0)
      val factor = lastValue - secondLastValue
      (params ::: List.tabulate(boardHeight - params.length + 1) (((n) => lastValue + (n+1) * factor))).toArray
    }
  }
}
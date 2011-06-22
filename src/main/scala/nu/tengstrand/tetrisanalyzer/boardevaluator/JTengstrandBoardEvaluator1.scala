package nu.tengstrand.tetrisanalyzer.boardevaluator

import nu.tengstrand.tetrisanalyzer.board.{Board, BoardOutline}

/**
 * Board evaluator created by Joakim Tengstrand.
 */
class JTengstrandBoardEvaluator1(boardWidth: Int = 10, boardHeight: Int = 20) extends BoardEvaluator {
  val heightFactor = calculateHeightFactor
  val blocksPerRowHollowFactor = calculateBlocksPerRowHollowFactor

  private val areaWidthFactor = calculateAreaWidthFactor

  val areaHeightFactor = calculateAreaHeightFactor
  val areaHeightFactorEqualWallHeight = calculateAreaHeightFactorEqualWallHeight

  val minBoardWidth = 4
  val maxBoardWidth = 32
  val minBoardHeight = 4
  val maxBoardHeight = 100

  def calculateHeightFactor: Array[Double] = {
    val head = List[Double](7, 7, 2.5, 2.2, 1.8, 1.3, 1.0, 0.9, 0.7, 0.6, 0.5, 0.4, 0.3, 0.25, 0.2, 0.15, 0.1, 0.1, 0.1, 0.1, 0.1, 0.09)

    if (boardHeight < head.length)
      head.take(boardHeight + 1).toArray
    else {
      val lastValue = head.takeRight(1)(0)
      val secondLastValue = head.takeRight(2)(0)
      val factor = lastValue / secondLastValue
      (head ::: List.tabulate(boardHeight - head.length + 1) (((n) => powSeries(lastValue, factor, n)))).toArray
    }
  }

  def calculateBlocksPerRowHollowFactor: Array[Double] = {
    val tail = List[Double](0, 0, 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.553)

    if (boardWidth > tail.length)
      (List.fill(boardWidth - tail.length) { 0.0 } ::: tail).toArray
    else
      tail.takeRight(boardWidth).toArray
  }

  def calculateAreaWidthFactor: Array[Double] = {
    val head = List[Double](0, 4.25, 2.39, 3.1, 2.21, 2.05, 1.87, 1.52, 1.34, 1.18, 1 )

    if (boardWidth < head.size)
      head.take(boardWidth + 1).toArray
    else {
      val factor = head.takeRight(1)(0) / head.takeRight(2)(0)
      (head ::: List.tabulate(boardWidth - head.length + 1) (((n) => powSeries(head.takeRight(1)(0), factor, n)))).toArray
    }
  }

  def calculateAreaHeightFactor(): Array[Double] = calculateAreaHeightFactor(List(0, 0.5, 1.19, 2.3, 3.1, 4.6, 5.6))
  def calculateAreaHeightFactorEqualWallHeight(): Array[Double] = calculateAreaHeightFactor(List(0, 0.42, 1.05, 2.2, 3.1, 4.6, 5.6))

  private def calculateAreaHeightFactor(params: List[Double]): Array[Double] = {
    if (boardHeight < params.length)
      params.take(boardHeight + 1).toArray
    else {
      val lastValue = params.takeRight(1)(0)
      val secondLastValue = params.takeRight(2)(0)
      val factor = lastValue - secondLastValue
      (params ::: List.tabulate(boardHeight - params.length + 1) (((n) => lastValue + (n+1) * factor))).toArray
    }
  }

  private def powSeries(initialValue: Double, factor: Double, n: Int) = (initialValue * scala.math.pow(factor, n + 1) * 100).round / 100.0

  def evaluate(board: Board): Double = {
    require(board.width <= boardWidth, "Can not evaluate board width > " + boardWidth)
    require(board.height <= boardHeight, "Can not evaluate board height > " + boardHeight)

    val outline = BoardOutline(board)

    evaluateBasedOnHollows(board, outline) +
    evaluateBasedOnOutlineHeight(outline) +
    evaluateBasedOnOutlineStructure(outline)
  }

  private def evaluateBasedOnHollows(board: Board, outline: BoardOutline) = {
    var equity = 0.0
    val hollowFactorForRow = Array.ofDim[Double](boardHeight + 1)

    var y = outline.minY
    while (y < boardHeight) {
      var numberOfBlocksPerRow = 0
      var minOutlineForHole = boardHeight

      var x = 0
      while (x < boardWidth) {
        if (!board.isFree(x, y))
          numberOfBlocksPerRow += 1
        else if (outline.get(x) < minOutlineForHole && outline.get(x) < y)
          minOutlineForHole = outline.get(x)
        x += 1
      }
      hollowFactorForRow(y) = blocksPerRowHollowFactor(numberOfBlocksPerRow);

      if (minOutlineForHole < boardHeight) {
        var hollowFactor = 1.0

        for (row <- minOutlineForHole to y)
          hollowFactor *= hollowFactorForRow(row)

        equity += (1 - hollowFactor) * boardWidth
      }
      y += 1
    }
    equity
  }

  private def evaluateBasedOnOutlineHeight(outline: BoardOutline): Double = {
    outline.outline.foldLeft(0.0) {(sum, y) => sum + heightFactor(y)} - heightFactor(outline.get(boardWidth))
  }

  private def evaluateBasedOnOutlineStructure(outline: BoardOutline): Double = {
    var equity = 0.0

    var x = 1
    while (x <= boardWidth) {
      var hasAreaWallsSameHeight = false
      var isAreaWallsSameHeightNotInitialized = true
      var areaHeight = 0
      var previousAreaWidth = 0

      var rightWallY = outline.get(x)
      var y = if (x == boardWidth) outline.minY else outline.get(x)

      // Calculate the size of the closed area in the outline (areaWidth * areaHeight).
      var continueCountAreaHeight = true
      while (y <= boardHeight && continueCountAreaHeight) {
        var areaWidth = 0

        var continueCountAreaWidth = true
        var areaX = x - 1
        while (areaX >= 0 && continueCountAreaWidth) {
          if (outline.get(areaX) <= y) {
            if (isAreaWallsSameHeightNotInitialized) {
              hasAreaWallsSameHeight = outline.get(areaX) == rightWallY
              isAreaWallsSameHeightNotInitialized = false
            }
            continueCountAreaWidth = false
          } else {
            areaWidth += 1
          }
          areaX -= 1
        }
        if (areaWidth == 0 && previousAreaWidth == 0) {
          continueCountAreaHeight = false
        } else {
          if (areaWidth > 0 && (areaWidth == previousAreaWidth || previousAreaWidth == 0)) {
            areaHeight += 1
          } else {
            if (hasAreaWallsSameHeight) {
              equity += areaWidthFactor(previousAreaWidth) * areaHeightFactorEqualWallHeight(areaHeight)
            } else {
              equity += areaWidthFactor(previousAreaWidth) * areaHeightFactor(areaHeight)
            }
            areaHeight = 1
            isAreaWallsSameHeightNotInitialized = true
          }
          previousAreaWidth = areaWidth;
        }
        y += 1
      }
      x += 1
    }
    equity
  }
}

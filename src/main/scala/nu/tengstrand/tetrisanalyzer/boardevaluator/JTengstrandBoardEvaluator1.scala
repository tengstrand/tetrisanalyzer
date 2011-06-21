package nu.tengstrand.tetrisanalyzer.boardevaluator

import nu.tengstrand.tetrisanalyzer.board.{Board, BoardOutline}

/**
 * Board evaluator created by Joakim Tengstrand.
 */
class JTengstrandBoardEvaluator1(boardWidth: Int = 10, boardHeight: Int = 20) extends BoardEvaluator {
  val heightFactor = Array[Double](7, 7, 2.5, 2.2, 1.8, 1.3, 1.0, 0.9, 0.7, 0.6, 0.5, 0.4, 0.3, 0.25, 0.2, 0.15, 0.1, 0.1, 0.1, 0.1, 0.1, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05  )
  val blocksPerRowHollowFactor = calculateBlocksPerRowHollowFactor

  val areaWidthFactorFactor = 0.88
  private val areaWidthFactor = calculateAreaWidthFactor

  val areaHeightFactor = Array[Double](0, .5, 1.19, 2.3, 3.1, 4.6, 5.6, 6.6, 7.6, 8.6, 9.6, 10.6, 11.6, 12.6, 13.6, 14.6, 15.6, 16.6, 17.6, 18.6, 19.6, 20.6, 21.6, 22.6, 23.6, 24.6, 25.6, 26.6, 27.6, 28.6, 29.6, 30.6, 31.6)
  val areaHeightFactorEqualWallHeight = Array[Double](0, .42, 1.05, 2.2, 3.1, 4.6, 5.6, 6.6, 7.6, 8.6, 9.6, 10.6, 11.6, 12.6, 13.6, 14.6, 15.6, 16.6, 17.6, 18.6, 19.6, 20.6, 21.6, 22.6, 23.6, 24.6, 25.6, 26.6, 27.6, 28.6, 29.6, 30.6, 31.6)

  val minBoardWidth = 4
  val maxBoardWidth = 32
  val minBoardHeight = 4
  val maxBoardHeight = 32

  def calculateBlocksPerRowHollowFactor = {
    val tail = Array[Double](0, 0, 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.553)

    val result = Array.fill[Double](boardWidth) { 0.0 }
    val startIndex = if (boardWidth < tail.length) 0 else boardWidth - tail.length
    val length = if (boardWidth > tail.length) tail.length else boardWidth
    tail.copyToArray(result, startIndex, length)
    result
  }

  def calculateAreaWidthFactor: Array[Double] = {
    val head = Array[Double](0, 4.25, 2.39, 3.1, 2.21, 2.05, 1.87, 1.52, 1.34, 1.18, 1 )

    val width = if (boardWidth + 1 < head.size) head.size else boardWidth + 1
    val result = Array.ofDim[Double](width)
    head.copyToArray(result, 0)

    var factor = head(head.size - 1)
    for (i <- head.size until width) {
      factor *= areaWidthFactorFactor
      result(i) = factor
    }
    result
  }

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
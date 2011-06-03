package nu.tengstrand.tetrisanalyzer.boardevaluator

import nu.tengstrand.tetrisanalyzer.board.{Board, BoardOutline}

/**
 * Board evaluator created by Joakim Tengstrand.
 */
class JTengstrandBoardEvaluator1(boardWidth: Int = 10, boardHeight: Int = 20) extends BoardEvaluator {
  require(boardWidth <= 10, "Only board widths between 4 and 10 is supported at the moment, but was: " + boardWidth)
  require(boardHeight <= 20, "Only board heights between 4 and 20 is supported at the moment, but was: " + boardHeight)

  val heightFactor = Array[Double](7, 7, 2.5, 2.2, 1.8, 1.3, 1.0, 0.9, 0.7, 0.6, 0.5, 0.4, 0.3, 0.25, 0.2, 0.15, 0.1, 0.1, 0.1, 0.1, 0.1)
  val blocksPerLineHollowFactor = Array[Double](0, 0, 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.553)
  val areaWidthFactor = Array[Double](0, 4.25, 2.39, 3.1, 2.21, 2.05, 1.87, 1.52, 1.34, 1.18, 0)
  val areaHeightFactor = Array[Double](0, .5, 1.19, 2.3, 3.1, 4.6, 5.6, 6.6, 7.6, 8.6, 9.6, 10.6, 11.6, 12.6, 13.6, 14.6, 15.6, 16.6, 17.6, 18.6, 19.6)
  val areaHeightFactorEqualWallHeight = Array[Double](0, .42, 1.05, 2.2, 3.1, 4.6, 5.6, 6.6, 7.6, 8.6, 9.6, 10.6, 11.6, 12.6, 13.6, 14.6, 15.6, 16.6, 17.6, 18.6, 19.6)

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
    val hollowFactorForLine = Array.ofDim[Double](boardHeight + 1)

    var y = outline.minY
    while (y < boardHeight) {
      var numberOfBlocksPerLine = 0
      var minOutlineForHole = boardHeight

      var x = 0
      while (x < boardWidth) {
        if (!board.isFree(x, y))
          numberOfBlocksPerLine += 1
        else if (outline.get(x) < minOutlineForHole && outline.get(x) < y)
          minOutlineForHole = outline.get(x)
        x += 1
      }
      hollowFactorForLine(y) = blocksPerLineHollowFactor(numberOfBlocksPerLine);

      if (minOutlineForHole < boardHeight) {
        var hollowFactor = 1.0

        for (line <- minOutlineForHole to y)
          hollowFactor *= hollowFactorForLine(line)

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
package nu.tengstrand.tetrisanalyzer.boardevaluator

import nu.tengstrand.tetrisanalyzer.board.{Board, BoardOutline}

/**
 * Board evaluator created by Joakim Tengstrand.
 */
class JTengstrandBoardEvaluator1(settings: JTengstrandBoardEvaluator1Settings, boardWidth: Int = 10, boardHeight: Int = 20) extends BoardEvaluator {
  private val heightFactor = settings.calculateHeightFactor(boardHeight)
  private val blocksPerRowHollowFactor = settings.calculateBlocksPerRowHollowFactor(boardWidth)
  private val areaWidthFactor = settings.calculateAreaWidthFactor(boardWidth)
  private val areaHeightFactor = settings.calculateAreaHeightFactor(boardHeight)
  private val areaHeightFactorEqualWallHeight = settings.calculateAreaHeightFactorEqualWallHeight(boardHeight)

  val minBoardWidth = 4
  val maxBoardWidth = 32
  val minBoardHeight = 4
  val maxBoardHeight = 200

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

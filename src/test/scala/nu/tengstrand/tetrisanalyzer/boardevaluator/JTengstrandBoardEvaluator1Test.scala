package nu.tengstrand.tetrisanalyzer.boardevaluator

import org.junit.Test
import nu.tengstrand.tetrisanalyzer.BaseTest
import nu.tengstrand.tetrisanalyzer.board.Board

class JTengstrandBoardEvaluator1Test extends BaseTest {

  @Test def evaluateVersionOneDotOne {
    val evaluator = new JTengstrandBoardEvaluator1(10, 5)

    val board = Board(Array(
        "#----------#",
        "#----------#",
        "#-x---xx---#",
        "#-xx-xx-x--#",
        "#xxxx-xxx--#",
        "############"
      ))

    evaluator.evaluate(board) should be (51.0386)
  }

  @Test def calculateBlocksPerRowHollowFactor_width15 {
    val evaluator = new JTengstrandBoardEvaluator1(15, 5)
    val result = evaluator.calculateBlocksPerRowHollowFactor

    result should be (Array(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.553))
  }

  @Test def calculateBlocksPerRowHollowFactor_width8 {
    val evaluator = new JTengstrandBoardEvaluator1(8, 5)
    val result = evaluator.calculateBlocksPerRowHollowFactor

    result should be (Array(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.553))
  }

  @Test def calculateAreaWidthFactor_width5 {
    val evaluator = new JTengstrandBoardEvaluator1(5, 5)
    val result = evaluator.calculateAreaWidthFactor

    result should be (Array(0.0, 4.25, 2.39, 3.1, 2.21, 2.05))
  }

  @Test def calculateAreaWidthFactor_width15 {
    val evaluator = new JTengstrandBoardEvaluator1(15, 5)
    val result = evaluator.calculateAreaWidthFactor

    result should be (Array(0.0, 4.25, 2.39, 3.1, 2.21, 2.05, 1.87, 1.52, 1.34, 1.18, 1.0, 0.85, 0.72, 0.61, 0.52, 0.44))
  }

  @Test def calculateAreaHeightFactor_height4 {
    val evaluator = new JTengstrandBoardEvaluator1(10, 4)
    val result = evaluator.calculateAreaHeightFactor

    result should be (Array(0, 0.5, 1.19, 2.3, 3.1))
  }

  @Test def calculateAreaHeightFactor_height10 {
    val evaluator = new JTengstrandBoardEvaluator1(8, 10)
    val result = evaluator.calculateAreaHeightFactor

    result should be (Array(0, 0.5, 1.19, 2.3, 3.1, 4.6, 5.6, 6.6, 7.6, 8.6, 9.6))
  }

  @Test def calculateHeightFactor_height5 {
    val evaluator = new JTengstrandBoardEvaluator1(10, 5)
    val result = evaluator.calculateHeightFactor

    result should be (Array(7, 7, 2.5, 2.2, 1.8, 1.3))
  }

  @Test def calculateHeightFactor_height25 {
    val evaluator = new JTengstrandBoardEvaluator1(10, 25)
    val result = evaluator.calculateHeightFactor

    result should be (Array(7.0, 7.0, 2.5, 2.2, 1.8, 1.3, 1.0, 0.9, 0.7, 0.6, 0.5, 0.4, 0.3, 0.25, 0.2, 0.15, 0.1, 0.1, 0.1, 0.1, 0.1, 0.09, 0.08, 0.07, 0.07, 0.06))
  }
}

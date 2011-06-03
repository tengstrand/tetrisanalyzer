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
}
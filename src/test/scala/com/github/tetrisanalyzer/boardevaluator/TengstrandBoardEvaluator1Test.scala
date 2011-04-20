package com.github.tetrisanalyzer.boardevaluator

import org.junit.Test
import com.github.tetrisanalyzer.BaseTest
import com.github.tetrisanalyzer.board.Board

class TengstrandBoardEvaluator1Test extends BaseTest {

  @Test def evaluateVersionOneDotOne {
    val evaluator = new TengstrandBoardEvaluator1(10, 5)

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
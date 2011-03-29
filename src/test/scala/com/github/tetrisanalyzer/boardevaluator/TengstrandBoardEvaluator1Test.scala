package com.github.tetrisanalyzer.boardevaluator

import org.junit.Test
import com.github.tetrisanalyzer.BaseTest
import com.github.tetrisanalyzer.board.Board

class TengstrandBoardEvaluator1Test extends BaseTest {

  @Test def evaluateVersionOneDotOne {
    val evaluator = new TengstrandBoardEvaluator1

    evaluator.evaluate(getBoard) should be (51.0386)
  }

  private def getBoard() =
    Board(Array(
      "#----------#",
      "#----------#",
      "#-x---xx---#",
      "#-xx-xx-x--#",
      "#xxxx-xxx--#",
      "############"
    ))
}
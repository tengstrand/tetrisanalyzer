package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test

class BoardSizeTest extends BaseTest {

  private val defaultBoardSize = new BoardSize(new MinMax(10, 4, 32), new MinMax(20, 4, 100))

  @Test def increase(): Unit = {
    val minMax = new MinMax(10, 4, 20)
    minMax.increase()
    minMax.value should be (11)
  }

  @Test def increase_reachedMax(): Unit = {
    val minMax = new MinMax(20, 4, 20)
    minMax.increase()
    minMax.value should be (20)
  }

  @Test def decrease(): Unit = {
    val minMax = new MinMax(10, 4, 20)
    minMax.decrease()
    minMax.value should be (9)
  }

  @Test def decrease_reachedMin(): Unit = {
    val minMax = new MinMax(4, 4, 20)
    minMax.decrease()
    minMax.value should be (4)
  }

  @Test def increaseHeight(): Unit = {
    val boardSize = defaultBoardSize
    boardSize.increaseHeight()
    boardSize.size should be (Size(10, 21))
  }

  @Test def decreaseHeight(): Unit = {
    val boardSize = defaultBoardSize
    boardSize.decreaseHeight()
    boardSize.size should be (Size(10, 19))
  }
}
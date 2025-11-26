package nu.tengstrand.tetrisanalyzer.board

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test

class BoardTest extends BaseTest {

  @Test def tooLow(): Unit = {
    assertThrows[IllegalArgumentException] {
      Board(Array(
        "#----------#",
        "#----x-----#",
        "#-x--x----x#",
        "############"))
    }
  }

  @Test def tooNarrow(): Unit = {
    assertThrows[IllegalArgumentException] {
      Board(Array(
        "#---#",
        "#---#",
        "#---#",
        "#-x-#",
        "#####"))
    }
  }

  @Test def tooWide(): Unit = {
    assertThrows[IllegalArgumentException] {
      Board(33,10)
    }
  }

  @Test def testToString(): Unit = {
    val boardArray = Array(
      "#----------#",
      "#----------#",
      "#----x-----#",
      "#-x--x----x#",
      "############")

    val board = Board(boardArray)

    board.toString should be (boardArray.mkString("\n"))
  }

  @Test def _IsFree_occupied(): Unit = {
    val board = Board(Array(
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#-x--------#",
      "############"))

    board.isFree(1,4) should be (false)
  }

  @Test def _IsFree(): Unit = {
    val board = Board(Array(
      "#xxxxxxxxxx#",
      "#xxxxxxxxxx#",
      "#xxxxxxxxxx#",
      "#xxxxxxxxxx#",
      "#x-xxxxxxxx#",
      "############"))

    board.isFree(1,4) should be (true)
  }

  @Test def clearRows(): Unit = {
    val board = Board(Array(
      "#----------#",
      "#----x-----#",
      "#xxxxxxxxxx#",
      "#xxxxxxxxxx#",
      "#-x--x----x#",
      "#xxxxxxxxxx#",
      "############"))

    board.clearRows(2, 4) should be (3)

    board should be (Board(Array(
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----x-----#",
      "#-x--x----x#",
      "############")))
  }

  @Test def copy(): Unit = {
    val board = getBoard

    board.copy should be (board)
  }

  @Test def restore(): Unit = {
    val empty = Board(8,4)
    val copy = getBoard
    empty.restore(copy)

    empty should be (copy)
  }

  private def getBoard = {
    Board(Array(
      "#x-------#",
      "#x-------#",
      "#xxx----x#",
      "#xxx-x-xx#",
      "##########"))
  }

  @Test
  def junkBoard(): Unit = {
    val board = Board(10, 5)

    board.junkBoard should be (Board(Array(
      "#-x-x-x-x-x#",
      "#x-x-x-x-x-#",
      "#-x-x-x-x-x#",
      "#x-x-x-x-x-#",
      "#-x-x-x-x-x#",
      "############")))
  }
}

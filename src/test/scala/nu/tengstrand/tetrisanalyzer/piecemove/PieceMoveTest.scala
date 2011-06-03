package nu.tengstrand.tetrisanalyzer.piecemove

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.move.Move
import nu.tengstrand.tetrisanalyzer.piece.{PieceI, PieceS}

class PieceMoveTest extends BaseTest {

  @Test def setPiece {
    val board = Board(8,4)
    val piece = new PieceS
    val move = Move(0,3, 1)

    PieceMove(board, piece, move).setPiece()

    board should be (Board(Array(
      "#--------#",
      "#----xx--#",
      "#---xx---#",
      "#--------#",
      "##########")))
  }

  @Test def setPiece_clearTwoLines {
    val board = Board(Array(
      "#----------#",
      "#----x-----#",
      "#xxxxxxxxxx#",
      "#-x--x----x#",
      "#xxxxxxxxxx#",
      "############"))
    val piece = new PieceI
    val move = Move(1,4, 1)

    PieceMove(board, piece, move).setPiece() should be (2)

    board should be (Board(Array(
      "#----------#",
      "#----------#",
      "#----------#",
      "#----x-----#",
      "#-x--x----x#",
      "############")))
  }

  @Test def clearPiece {
    val board = Board(Array(
      "#xxxxxxxx#",
      "#xxxxxxxx#",
      "#xxxxxxxx#",
      "#xxxxxxxx#",
      "##########"))
    val piece = new PieceS
    val move = Move(0,3, 1)

    PieceMove(board, piece, move).clearPiece

    board should be (Board(Array(
      "#xxxxxxxx#",
      "#xxxx--xx#",
      "xxxx--xxx#",
      "#xxxxxxxx#",
      "##########")))
  }

  @Test def isFree {
    val board = Board(Array(
      "#-xxxxxxx#",
      "#-xxxxxxx#",
      "#xxxx--xx#",
      "#xxx--xxx#",
      "##########"))

    val piece = new PieceS
    val move = Move(0,3, 2)

    PieceMove(board, piece, move).isFree should be (true)
  }

  @Test def isFree_occupied {
    val board = Board(Array(
      "#--------#",
      "#--------#",
      "#--------#",
      "#----x---#",
      "##########"))

    val piece = new PieceS
    val move = Move(0,3, 2)

    PieceMove(board, piece, move).isFree should be (false)
  }
}
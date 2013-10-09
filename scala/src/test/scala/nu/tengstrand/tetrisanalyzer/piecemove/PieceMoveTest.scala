package nu.tengstrand.tetrisanalyzer.piecemove

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.move.Move
import nu.tengstrand.tetrisanalyzer.piece.{PieceI, PieceS}
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings
import collection.mutable.LinkedHashSet

class PieceMoveTest extends BaseTest {

  @Test def setPiece() {
    val board = Board(8,4)
    val piece = PieceS()
    val move = Move(0,3, 1)

    PieceMove(board, piece, move).setPiece()

    board should be (Board(Array(
      "#--------#",
      "#----xx--#",
      "#---xx---#",
      "#--------#",
      "##########")))
  }

  @Test def setStandingPieceI() {
    val board = Board(8,4)
    val piece = PieceI()
    val move = Move(1,0, 0)

    PieceMove(board, piece, move).setPiece()

    board should be (Board(Array(
      "#x-------#",
      "#x-------#",
      "#x-------#",
      "#x-------#",
      "##########")))
  }

  @Test def setPiece_clearTwoRows() {
    val board = Board(Array(
      "#----------#",
      "#----x-----#",
      "#xxxxxxxxxx#",
      "#-x--x----x#",
      "#xxxxxxxxxx#",
      "############"))
    val piece = PieceI()
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

  @Test def clearPiece() {
    val board = Board(Array(
      "#xxxxxxxx#",
      "#xxxxxxxx#",
      "#xxxxxxxx#",
      "#xxxxxxxx#",
      "##########"))
    val piece = PieceS()
    val move = Move(0,3, 1)

    PieceMove(board, piece, move).clearPiece()

    board should be (Board(Array(
      "#xxxxxxxx#",
      "#xxxx--xx#",
      "xxxx--xxx#",
      "#xxxxxxxx#",
      "##########")))
  }

  @Test def _isFree() {
    val board = Board(Array(
      "#-xxxxxxx#",
      "#-xxxxxxx#",
      "#xxxx--xx#",
      "#xxx--xxx#",
      "##########"))

    val piece = PieceS()
    val move = Move(0,3, 2)

    PieceMove(board, piece, move).isFree should be (true)
  }

  @Test def _isFree_occupied() {
    val board = Board(Array(
      "#--------#",
      "#--------#",
      "#--------#",
      "#----x---#",
      "##########"))

    val piece = PieceS()
    val move = Move(0,3, 2)

    PieceMove(board, piece, move).isFree should be (false)
  }

  @Test def calculateAnimatedPath() {
    val board = Board(Array(
      "#-----#",
      "#-----#",
      "#-----#",
      "#----x#",
      "#----x#",
      "#######"))
    val piece = PieceS()
    val settings = new DefaultGameSettings
    val validPieceMovesForEmptyBoard = new ValidPieceMovesForEmptyBoard(board, piece, settings)
    var pieceMove = validPieceMovesForEmptyBoard.startMove

    pieceMove.calculateAnimatedPath(null, 0, 0)

    pieceMove = pieceMove.asideAndRotate.find(_.move.x == 0).get

    while (pieceMove.down != null)
      pieceMove = pieceMove.down

    var animatedPath = new LinkedHashSet[PieceMove]

    while (pieceMove != null) {
      animatedPath += pieceMove
      pieceMove = pieceMove.animatedPath
    }
    animatedPath should be (LinkedHashSet(
      PieceMove(board,piece,Move(0,0, 3)),
      PieceMove(board,piece,Move(0,0, 2)),
      PieceMove(board,piece,Move(0,0, 1)),
      PieceMove(board,piece,Move(0,0, 0)),
      PieceMove(board,piece,Move(0,1, 0))
    ))
  }
}
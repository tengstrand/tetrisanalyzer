package nu.tengstrand.tetrisanalyzer.piecemove

import org.junit.Test
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piece.PieceS
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings

import scala.collection.mutable.Set
import scala.collection.mutable.LinkedHashSet
import nu.tengstrand.tetrisanalyzer.BaseTest
import nu.tengstrand.tetrisanalyzer.move.Move

class ValidPieceMovesForEmptyBoardTest extends BaseTest {

  @Test def startMove(): Unit = {
    val board = Board(5,5)
    val piece = PieceS()
    val settings = new DefaultGameSettings
    val validPieceMovesForEmptyBoard = new ValidPieceMovesForEmptyBoard(board, piece, settings)

    var pieceMoves = new LinkedHashSet[PieceMove]
    addPieceMoves(validPieceMovesForEmptyBoard.startMove, pieceMoves)

    pieceMoves should be (LinkedHashSet(
      PieceMove(board,piece,Move(0,1, 0)),
      PieceMove(board,piece,Move(0,0, 0)),
      PieceMove(board,piece,Move(0,0, 1)),
      PieceMove(board,piece,Move(0,0, 2)),
      PieceMove(board,piece,Move(0,0, 3)),
      PieceMove(board,piece,Move(1,1, 0)),
      PieceMove(board,piece,Move(1,0, 0)),
      PieceMove(board,piece,Move(1,0, 1)),
      PieceMove(board,piece,Move(1,0, 2)),
      PieceMove(board,piece,Move(1,2, 0)),
      PieceMove(board,piece,Move(1,3, 0)),
      PieceMove(board,piece,Move(1,3, 1)),
      PieceMove(board,piece,Move(1,3, 2)),
      PieceMove(board,piece,Move(1,2, 1)),
      PieceMove(board,piece,Move(1,2, 2)),
      PieceMove(board,piece,Move(1,1, 1)),
      PieceMove(board,piece,Move(1,1, 2)),
      PieceMove(board,piece,Move(0,2, 0)),
      PieceMove(board,piece,Move(0,2, 1)),
      PieceMove(board,piece,Move(0,2, 2)),
      PieceMove(board,piece,Move(0,2, 3)),
      PieceMove(board,piece,Move(0,1, 1)),
      PieceMove(board,piece,Move(0,1, 2)),
      PieceMove(board,piece,Move(0,1, 3))
    ))
  }

  private def addPieceMoves(pieceMove: PieceMove, pieceMoves: Set[PieceMove]): Unit = {
    if (pieceMove != null && !pieceMoves.contains(pieceMove)) {
      pieceMoves += pieceMove
      pieceMove.asideAndRotate.foreach(move => addPieceMoves(move, pieceMoves))
      pieceMove.down.foreach(addPieceMoves(_, pieceMoves))
    }
  }
}

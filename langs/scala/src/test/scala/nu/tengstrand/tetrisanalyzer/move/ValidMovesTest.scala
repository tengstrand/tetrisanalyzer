package nu.tengstrand.tetrisanalyzer.move

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piecemove.{PieceMove, AllValidPieceMovesForEmptyBoard}
import nu.tengstrand.tetrisanalyzer.piece.PieceS

class ValidMovesTest extends BaseTest {

  @Test def pieceMoves(): Unit = {
    val board = Board(5,4)
    val validMoves = new ValidMoves(board)
    val startPiece = getStartPiece(board)
    val pieceMoves = validMoves.pieceMoves(startPiece)
    val moves = for { pieceMove <- pieceMoves } yield pieceMove.move

    moves.sortBy{m => (m.x, m.rotation, m.y)} should be (List(
      Move(0,0, 2),
      Move(1,0, 1),
      Move(0,1, 2),
      Move(1,1, 1),
      Move(0,2, 2),
      Move(1,2, 1),
      Move(1,3, 1))
    )
  }

  private def getStartPiece(board: Board): PieceMove = {
    val piece = PieceS()
    val settings = new DefaultGameSettings
    val allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings)

    allValidPieceMoves.startMoveForPiece(piece)
  }
}
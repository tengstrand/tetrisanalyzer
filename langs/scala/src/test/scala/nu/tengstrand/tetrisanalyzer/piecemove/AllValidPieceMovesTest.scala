package nu.tengstrand.tetrisanalyzer.piecemove

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piece.PieceT
import nu.tengstrand.tetrisanalyzer.move.Move
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings

class AllValidPieceMovesTest extends BaseTest {

  @Test def startMoveForPiece(): Unit = {
    val board = Board(6,6)
    val piece = PieceT()
    val settings = new DefaultGameSettings
    val allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings)

    val startMove = allValidPieceMoves.startMoveForPiece(piece)

    startMove should be (PieceMove(board, piece, Move(0,2, 0)))
  }
}
package nu.tengstrand.tetrisanalyzer.move

import org.junit.Test
import nu.tengstrand.tetrisanalyzer.BaseTest
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings
import nu.tengstrand.tetrisanalyzer.piece.PieceS
import nu.tengstrand.tetrisanalyzer.piecemove.{AllValidPieceMovesForEmptyBoard, PieceMove}
import nu.tengstrand.tetrisanalyzer.boardevaluator.{JTengstrandBoardEvaluator1DefaultSettings, JTengstrandBoardEvaluator1}

class EvaluatedMovesTest extends BaseTest {
  private val board = Board()
  private val piece = new PieceS

  @Test def bestMove {
    getEvaluatedMoves.bestMove should be (
      Some(PieceMove(board, piece, Move(0,7, 18))))
  }

  private def getEvaluatedMoves = {
    val settings = new DefaultGameSettings
    val allValidPieceMovesForEmptyBoard = new AllValidPieceMovesForEmptyBoard(board, settings)
    val startPieceMove = allValidPieceMovesForEmptyBoard.startMoveForPiece(piece)
    val validMoves = new ValidMoves(board).pieceMoves(startPieceMove)
    val boardEvaluatorSettings = new JTengstrandBoardEvaluator1DefaultSettings
    val boardEvaluator = new JTengstrandBoardEvaluator1(boardEvaluatorSettings)
    val startPieceMoves = allValidPieceMovesForEmptyBoard.startPieces
    val maxEquity = boardEvaluator.evaluate(board.junkBoard)
    new EvaluatedMoves(board, validMoves, boardEvaluator, startPieceMoves, settings.firstFreeRowUnderStartPiece, maxEquity)
  }

  @Test def evaluatedMoves {
    val moves: List[MoveEquity] = getEvaluatedMoves.sortedMovesWithAdjustedEquity

    moves should be (List(
      MoveEquity(PieceMove(board, piece, Move(0,7, 18)), 12.430),
      MoveEquity(PieceMove(board, piece, Move(1,0, 17)), 0.755),
      MoveEquity(PieceMove(board, piece, Move(0,0, 18)), 2.504),
      MoveEquity(PieceMove(board, piece, Move(0,5, 18)), 3.374),
      MoveEquity(PieceMove(board, piece, Move(0,2, 18)), 3.755),
      MoveEquity(PieceMove(board, piece, Move(1,8, 17)), 3.777),
      MoveEquity(PieceMove(board, piece, Move(0,3, 18)), 3.855),
      MoveEquity(PieceMove(board, piece, Move(0,1, 18)), 4.115),
      MoveEquity(PieceMove(board, piece, Move(0,4, 18)), 4.389),
      MoveEquity(PieceMove(board, piece, Move(0,6, 18)), 5.323),
      MoveEquity(PieceMove(board, piece, Move(1,2, 17)), 7.052),
      MoveEquity(PieceMove(board, piece, Move(1,6, 17)), 7.265),
      MoveEquity(PieceMove(board, piece, Move(1,4, 17)), 7.308),
      MoveEquity(PieceMove(board, piece, Move(1,5, 17)), 8.079),
      MoveEquity(PieceMove(board, piece, Move(1,7, 17)), 8.319),
      MoveEquity(PieceMove(board, piece, Move(1,3, 17)), 9.075),
      MoveEquity(PieceMove(board, piece, Move(1,1, 17)), 10.824)))
  }
}

package nu.tengstrand.tetrisanalyzer.move

import org.junit.Test
import nu.tengstrand.tetrisanalyzer.BaseTest
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings
import nu.tengstrand.tetrisanalyzer.piecemove.{AllValidPieceMovesForEmptyBoard, PieceMove}
import nu.tengstrand.tetrisanalyzer.boardevaluator.{JTengstrandBoardEvaluator1DefaultSettings, JTengstrandBoardEvaluator1}
import nu.tengstrand.tetrisanalyzer.game.startpiece.StartPiece
import nu.tengstrand.tetrisanalyzer.piece.{PieceL, PieceS}

class EvaluatedMovesTest extends BaseTest {
  private val piece = PieceS()

  @Test def bestMove {
    val board = Board()
    val startPiece = new StartPiece(piece)
    getEvaluatedMoves(board, startPiece).bestMove should be (
      Some(PieceMove(board, piece, Move(0,7, 18))))
  }

  @Test def evaluatedMovesOnePiece {
    val board = Board()
    val startPiece = new StartPiece(piece)
    val evaluatedMoves = getEvaluatedMoves(board, startPiece)
    val moves: List[MoveEquity] = evaluatedMoves.sortedMovesWithAdjustedEquity

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

  @Test def evaluatedMovesTwoPieces {
    val board = Board(Array(
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#----------#",
      "#--x-------#",
      "#-xxx------#",
      "############"))

    val startPiece = new StartPiece(PieceL(), PieceS())
    val evaluatedMoves = getEvaluatedMoves(board, startPiece)
    val moves: List[MoveEquity] = evaluatedMoves.sortedMovesWithAdjustedEquity
    val piece = startPiece.firstPiece

    moves should be (List(
      MoveEquity(PieceMove(board, piece, Move(2,7, 18)), 7.468),
      MoveEquity(PieceMove(board, piece, Move(1,7, 17)), 2.392),
      MoveEquity(PieceMove(board, piece, Move(2,5, 18)), 3.185),
      MoveEquity(PieceMove(board, piece, Move(1,5, 17)), 4.367),
      MoveEquity(PieceMove(board, piece, Move(2,6, 18)), 4.698),
      MoveEquity(PieceMove(board, piece, Move(1,4, 17)), 4.876),
      MoveEquity(PieceMove(board, piece, Move(0,7, 18)), 6.256),
      MoveEquity(PieceMove(board, piece, Move(1,6, 17)), 6.784),
      MoveEquity(PieceMove(board, piece, Move(2,4, 18)), 6.792),
      MoveEquity(PieceMove(board, piece, Move(1,0, 16)), 7.099),
      MoveEquity(PieceMove(board, piece, Move(1,8, 17)), 8.076),
      MoveEquity(PieceMove(board, piece, Move(0,4, 18)), 8.822),
      MoveEquity(PieceMove(board, piece, Move(0,5, 18)), 10.981),
      MoveEquity(PieceMove(board, piece, Move(1,3, 16)), 11.572),
      MoveEquity(PieceMove(board, piece, Move(2,3, 17)), 11.704),
      MoveEquity(PieceMove(board, piece, Move(0,6, 18)), 13.068),
      MoveEquity(PieceMove(board, piece, Move(0,0, 17)), 16.359),
      MoveEquity(PieceMove(board, piece, Move(3,8, 17)), 17.037),
      MoveEquity(PieceMove(board, piece, Move(2,0, 16)), 18.621),
      MoveEquity(PieceMove(board, piece, Move(3,3, 17)), 19.572),
      MoveEquity(PieceMove(board, piece, Move(0,3, 17)), 20.08),
      MoveEquity(PieceMove(board, piece, Move(2,2, 16)), 21.189),
      MoveEquity(PieceMove(board, piece, Move(3,4, 17)), 22.334),
      MoveEquity(PieceMove(board, piece, Move(3,6, 17)), 22.397),
      MoveEquity(PieceMove(board, piece, Move(1,2, 15)), 23.012),
      MoveEquity(PieceMove(board, piece, Move(3,7, 17)), 24.036),
      MoveEquity(PieceMove(board, piece, Move(3,5, 17)), 24.872),
      MoveEquity(PieceMove(board, piece, Move(3,2, 16)), 25.434),
      MoveEquity(PieceMove(board, piece, Move(0,1, 17)), 26.097),
      MoveEquity(PieceMove(board, piece, Move(2,1, 16)), 26.604),
      MoveEquity(PieceMove(board, piece, Move(3,0, 16)), 27.289),
      MoveEquity(PieceMove(board, piece, Move(1,1, 15)), 28.101),
      MoveEquity(PieceMove(board, piece, Move(0,2, 16)), 31.472),
      MoveEquity(PieceMove(board, piece, Move(3,1, 15)), 48.365)))
  }

  private def getEvaluatedMoves(board: Board, startPiece: StartPiece) = {
    val settings = new DefaultGameSettings
    val allValidPieceMovesForEmptyBoard = new AllValidPieceMovesForEmptyBoard(board, settings)
    val startPieceMove = allValidPieceMovesForEmptyBoard.startMoveForPiece(startPiece.firstPiece)
    val nextPieceMove: PieceMove = if (startPiece.hasNext) allValidPieceMovesForEmptyBoard.startMoveForPiece(startPiece.secondPiece) else null
    val validMoves = new ValidMoves(board).pieceMoves(startPieceMove)
    val boardEvaluatorSettings = new JTengstrandBoardEvaluator1DefaultSettings
    val boardEvaluator = new JTengstrandBoardEvaluator1(boardEvaluatorSettings)
    val startPieceMoves = allValidPieceMovesForEmptyBoard.startPieces
    val maxEquity = boardEvaluator.evaluate(board.junkBoard)
    new EvaluatedMoves(board, validMoves, boardEvaluator, startPiece, nextPieceMove, startPieceMoves, settings.firstGuaranteedFreeRowUnderStartPiece, maxEquity)
  }
}

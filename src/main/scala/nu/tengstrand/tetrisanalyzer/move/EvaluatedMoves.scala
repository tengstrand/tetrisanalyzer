package nu.tengstrand.tetrisanalyzer.move

import nu.tengstrand.tetrisanalyzer.piecemove.PieceMove
import nu.tengstrand.tetrisanalyzer.boardevaluator.BoardEvaluator
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.piece.Piece

case class MoveEquity(pieceMove: PieceMove, equity: Double)

object EvaluatedMoves {
  def apply(board: Board, pieceMoves: List[PieceMove], boardEvaluator: BoardEvaluator,
            startPieceMoves: List[PieceMove], firstFreeRowUnderStartPiece: Int, maxEquity: Double) = {
    new EvaluatedMoves(board, pieceMoves, boardEvaluator, startPieceMoves, firstFreeRowUnderStartPiece, maxEquity)
  }
}


/**
 * Takes a list of piece moves and evaluates them using the given board evaluator.
 */
class EvaluatedMoves(board: Board, pieceMoves: List[PieceMove], boardEvaluator: BoardEvaluator,
                     startPieceMoves: List[PieceMove], firstGuaranteedFreeRowUnderStartPiece: Int, maxEquity: Double) {
  val moves: List[MoveEquity] = evaluateValidMoves

  /**
   * Returns the move (if any) that is ranked as number one.
   * It would be nice to replace this crappy code with:
   *     moves.reduceLeft((a, b) => (if (a.equity <= b.equity) a else b)).pieceMove
   * but this will slow down the over all speed with more than 20%!
   */
  def bestMove: Option[PieceMove] = {
    val best = bestMoveEquity

    if (!best.isDefined)
      None
    else
      Some(best.get.pieceMove)
  }

  def bestMoveEquity: Option[MoveEquity] = {
    if (moves.isEmpty) {
      None
    } else {
      var bestEquity = Double.MaxValue
      var bestMoveEquity: MoveEquity = null
      moves.foreach(moveEquity => if (moveEquity.equity < bestEquity) {
        bestEquity = moveEquity.equity
        bestMoveEquity = moveEquity
      })
      Some(bestMoveEquity)
    }
  }

  /**
   * Evaluates the equity of all valid moves for a given position.
   */
  private def evaluateValidMoves: List[MoveEquity] = {
    if (!pieceMoves.isEmpty) {
      val boardCopy = board.copy

      for {
        pieceMove <- pieceMoves
      } yield MoveEquity(pieceMove, evaluate(pieceMove, boardCopy))
    } else {
      List.empty[MoveEquity]
    }
  }

  private def evaluate(pieceMove: PieceMove, boardCopy: Board): Double = {
    val clearedRows = pieceMove.setPiece()
    var equity = boardEvaluator.evaluate(pieceMove.board)

    if (pieceMove.move.y + clearedRows < firstGuaranteedFreeRowUnderStartPiece)
       equity = adjustEquityIfAnyNextPieceIsOccupied(pieceMove.board, equity)

    if (clearedRows == 0)
      pieceMove.clearPiece()
    else
      pieceMove.board.restore(boardCopy)

    equity
  }

  private def adjustEquityIfAnyNextPieceIsOccupied(board: Board, equity: Double) = {
    startPieceMoves.foldLeft(0.0) { (sum,pieceMove) => sum + (if (pieceMove.isFree) equity else maxEquity) } / Piece.NumberOfPieceTypes
  }

  def sortedMovesWithAdjustedEquity: List[MoveEquity] = {
    def roundThreeDecimals(moveEquity: MoveEquity, bestEquity: Double) = {
      val equity = scala.math.round((moveEquity.equity - bestEquity) * 1000) / 1000.0
      MoveEquity(moveEquity.pieceMove, equity)
    }

    if (moves.isEmpty)
      List.empty[MoveEquity]
    else {
      val sortedMoves = moves.sortBy{m => (m.equity)}
      val bestMove = sortedMoves.head

      roundThreeDecimals(sortedMoves.head, 0) ::
      (for {
        moveEquity <- sortedMoves.tail
      } yield roundThreeDecimals(moveEquity, bestMove.equity))
    }
  }
}

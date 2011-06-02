package com.github.tetrisanalyzer.move

import com.github.tetrisanalyzer.piecemove.PieceMove
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.piece.Piece

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
                     startPieceMoves: List[PieceMove], firstFreeRowUnderStartPiece: Int, maxEquity: Double) {
  val moves: List[MoveEquity] = evaluateValidMoves

  /**
   * Returns the move (if any) that is ranked as number one.
   * It would be nice to replace this crappy code with:
   *     moves.reduceLeft((a, b) => (if (a.equity <= b.equity) a else b)).pieceMove
   * but this will slow down the over all speed with more than 20%!
   */
  def bestMove: Option[PieceMove] = {
    if (moves.isEmpty) {
      None
    } else {
      var bestEquity = Double.MaxValue
      var bestPieceMove: PieceMove = null
      moves.foreach(pieceMove => if (pieceMove.equity < bestEquity) {
        bestEquity = pieceMove.equity
        bestPieceMove = pieceMove.pieceMove
      })
      Some(bestPieceMove)
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
    val clearedLines = pieceMove.setPiece
    var equity = boardEvaluator.evaluate(pieceMove.board)

    if (pieceMove.move.y + clearedLines < firstFreeRowUnderStartPiece)
       equity = adjustEquityIfNextPieceIsOccupied(pieceMove.board, equity)

    if (clearedLines == 0)
      pieceMove.clearPiece()
    else
      pieceMove.board.restore(boardCopy)

    equity
  }

  private def adjustEquityIfNextPieceIsOccupied(board: Board, equity: Double) = {
    startPieceMoves.foldLeft(0.0) { (sum,pieceMove) => sum + (if (pieceMove.isFree) equity else maxEquity) } / Piece.NumberOfPieceTypes
  }
}

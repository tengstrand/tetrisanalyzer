package com.github.tetrisanalyzer.move

import com.github.tetrisanalyzer.piecemove.PieceMove
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator
import com.github.tetrisanalyzer.board.Board

case class MoveEquity(pieceMove: PieceMove, equity: Double)

object EvaluatedMoves {
  def apply(board: Board, pieceMoves: List[PieceMove], boardEvaluator: BoardEvaluator) = {
    new EvaluatedMoves(board, pieceMoves, boardEvaluator)
  }
}

/**
 * Takes a list of piece moves and evaluates them using the given board evaluator.
 */
class EvaluatedMoves(board: Board, pieceMoves: List[PieceMove], boardEvaluator: BoardEvaluator) {
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
    val equity = boardEvaluator.evaluate(pieceMove.board)

    if (clearedLines == 0)
      pieceMove.clearPiece()
    else
      pieceMove.board.restore(boardCopy)

    equity
  }
}

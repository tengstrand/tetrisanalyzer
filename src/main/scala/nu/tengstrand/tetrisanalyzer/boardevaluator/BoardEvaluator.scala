package nu.tengstrand.tetrisanalyzer.boardevaluator

import nu.tengstrand.tetrisanalyzer.board.Board

trait BoardEvaluator {

  /**
   * Evaluates the board, less is better.
   */
  def evaluate(board: Board): Double
}
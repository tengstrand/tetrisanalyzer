package com.github.tetrisanalyzer.boardevaluator

import com.github.tetrisanalyzer.board.Board

trait BoardEvaluator {

  /**
   * Evaluates the board, less is better.
   */
  def evaluate(board: Board): Double
}
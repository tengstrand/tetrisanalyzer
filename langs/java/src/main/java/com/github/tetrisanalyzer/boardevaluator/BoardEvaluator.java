package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;

public interface BoardEvaluator {

    /**
     * Evaluates the board, less is better.
     */
    double evaluate(Board board);

    String export();
}

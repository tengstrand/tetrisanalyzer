package nu.tengstrand.tetrisanalyzer.boardevaluator;

import nu.tengstrand.tetrisanalyzer.board.Board;

public interface BoardEvaluator {

    /**
     * Evaluates the board, less is better.
     */
    double evaluate(Board board);
}

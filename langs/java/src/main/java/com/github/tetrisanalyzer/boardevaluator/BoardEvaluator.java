package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;

public interface BoardEvaluator {
    double evaluate(Board board, AllValidPieceMoves allValidPieceMoves);
}

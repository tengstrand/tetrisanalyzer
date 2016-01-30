package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;

import java.util.Map;

public interface BoardEvaluator {
    double maxEquity();
    double evaluate(Board board, AllValidPieceMoves allValidPieceMoves);
    Map<String,String> parameters();
}

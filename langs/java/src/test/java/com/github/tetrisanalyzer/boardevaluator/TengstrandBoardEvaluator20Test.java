package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import com.github.tetrisanalyzer.settings.StandardGameSettings;
import org.junit.Test;

public class TengstrandBoardEvaluator20Test {

    @Test
    public void evaluate() {
        Board board = Board.create(
                "|----------|",
                "|----------|",
                "|xx--------|",
                "|-xx----xxx|",
                "|-xxx--xxxx|",
                "------------");

        BoardEvaluator evaluator = new TengstrandBoardEvaluator20(10, 5, new StandardGameSettings(board));
        AllValidPieceMoves allValidPieceMoves = new AllValidPieceMoves(board, new AtariGameSettings(board));

        double equity = evaluator.evaluate(board, allValidPieceMoves);

        System.out.println(equity);
    }
}
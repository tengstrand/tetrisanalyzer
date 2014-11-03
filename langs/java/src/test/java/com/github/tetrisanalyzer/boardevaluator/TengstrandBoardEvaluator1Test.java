package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TengstrandBoardEvaluator1Test {

    @Test
    public void evaluateVersionOneDotOne() {
        BoardEvaluator evaluator = new TengstrandBoardEvaluator1(10, 5);

        Board board = Board.create(
                "|----------|",
                "|----------|",
                "|-x---xx---|",
                "|-xx-xx-x--|",
                "|xxxx-xxxx-|",
                "¯¯¯¯¯¯¯¯¯¯¯¯");

        AllValidPieceMoves allValidPieceMoves = new AllValidPieceMoves(board, new AtariGameSettings(board));
        assertEquals(47.8145, evaluator.evaluate(board, allValidPieceMoves), 0.0001);
    }
}

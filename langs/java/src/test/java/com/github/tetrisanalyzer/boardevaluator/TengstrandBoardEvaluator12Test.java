package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TengstrandBoardEvaluator12Test {

    @Test
    @Ignore
    public void evaluateVersionOneDotOne() {
        BoardEvaluator evaluator = new TengstrandBoardEvaluator12(10, 5);

        Board board = Board.create(
                "|----------|",
                "|----------|",
                "|-x---xx---|",
                "|-xx-xx-x--|",
                "|xxxx-xxxx-|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");

        AllValidPieceMoves allValidPieceMoves = new AllValidPieceMoves(board, new AtariGameSettings(board));
        assertEquals(49.2915, evaluator.evaluate(board, allValidPieceMoves), 0.0001);
    }
}

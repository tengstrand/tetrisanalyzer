package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
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
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");

        assertEquals(51.7145, evaluator.evaluate(board), 0.0001);
    }
}

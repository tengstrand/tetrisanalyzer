package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TengstrandBoardEvaluator1Test {

    @Test
    @Ignore
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
        assertEquals(49.2915, evaluator.evaluate(board, allValidPieceMoves), 0.0001);
    }

    @Test
    @Ignore
    public void export() {
        BoardEvaluator evaluator = new TengstrandBoardEvaluator1(10, 5);

        String expected = "board evaluator:\n" +
                "  id: Tengstrand 1.1\n" +
                "  description: A first version was created 2001\n" +
                "  author: Joakim Tengstrand\n" +
                "  url: http://hem.bredband.net/joakimtengstrand\n" +
                "  class: com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1\n" +
                "  less is: better\n" +
                "  min board size: [4,4]\n" +
                "  max board size: [10,20]\n" +
                "  height factor: [7.0, 2.5, 2.2, 1.8, 1.3, 1.0, 0.9, 0.7, 0.6, 0.5, 0.4, 0.3, 0.25, 0.2, 0.15, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1]\n" +
                "  hollow factor: [0.0, 0.0, 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.553]\n" +
                "  area width factor: [0.0, 4.95, 2.39, 3.1, 2.21, 2.05, 1.87, 1.52, 1.34, 1.18, 0.0]\n" +
                "  area height factor: [0.0, 0.5, 1.19, 2.3, 3.1, 4.6, 5.6, 6.6, 7.6, 8.6, 9.6, 10.6, 11.6, 12.6, 13.6, 14.6, 15.6, 16.6, 17.6, 18.6, 19.6]\n" +
                "  area height factor2: [0.0, 0.42, 1.05, 2.2, 3.1, 4.6, 5.6, 6.6, 7.6, 8.6, 9.6, 10.6, 11.6, 12.6, 13.6, 14.6, 15.6, 16.6, 17.6, 18.6, 19.6]\n";

        assertEquals(expected, evaluator.export());
    }
}

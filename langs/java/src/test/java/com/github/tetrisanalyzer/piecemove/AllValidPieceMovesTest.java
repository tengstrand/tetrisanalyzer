package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import com.github.tetrisanalyzer.settings.GameSettings;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class AllValidPieceMovesTest {
    
    @Test
    public void adjustEquity_allSevenPiecesAreFree() {
        Board board = Board.create(10,20);
        GameSettings settings = new AtariGameSettings(board);
        AllValidPieceMoves allValidPieceMoves = new AllValidPieceMoves(board, settings);

        assertEquals(1.234, allValidPieceMoves.adjustEquityIfOccupiedStartPiece(1.234, 100.0, board));
    }

    @Test
    public void adjustEquity_xPieceIsOccupied() {
        Board board = Board.create(
                "|---x|",
                "|--xx|",
                "|--xx|",
                "|x-xx|",
                "¯¯¯¯¯¯");
        GameSettings settings = new AtariGameSettings(board);
        AllValidPieceMoves allValidPieceMoves = new AllValidPieceMoves(board, settings);

        assertEquals(43.562285714285714, allValidPieceMoves.adjustEquityIfOccupiedStartPiece(1.234, 100.0, board));
    }
}

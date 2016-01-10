package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.BoardOutline;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContourTest {

    @Test
    public void standingSdoesFit() {
        Board board = Board.create(
                "|--------|",
                "|--------|",
                "|-----x--|",
                "|-xx-xxx-|",
                "----------");

        BoardOutline outline = new BoardOutline(board, true);

        assertTrue(Contour.S1.fit(outline));
    }

    @Test
    public void layingSdoesNotFit() {
        Board board = Board.create(
                "|--------|",
                "|--------|",
                "|-x------|",
                "|-xx--x--|",
                "----------");

        BoardOutline outline = new BoardOutline(board, true);

        assertFalse(Contour.S0.fit(outline));
    }
}

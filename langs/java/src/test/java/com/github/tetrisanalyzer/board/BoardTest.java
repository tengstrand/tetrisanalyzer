package com.github.tetrisanalyzer.board;

import org.junit.Test;

import static com.github.tetrisanalyzer.board.Board.createBoard;
import static org.junit.Assert.*;

public class BoardTest {

    @Test(expected = IllegalArgumentException.class)
    public void tooLow() {
        createBoard(
                "|----------|",
                "|----x-----|",
                "|-x--x----x|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooNarrow() {
        createBoard(
                "|---|",
                "|---|",
                "|---|",
                "|-x-|",
                "ŻŻŻŻŻ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooWide() {
        createBoard(33, 10);
    }

    @Test
    public void testToString() {
        String[] expectedBoard = new String[]{
                "|----------|",
                "|----------|",
                "|----x-----|",
                "|-x--x----x|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ"};

        Board board = createBoard(expectedBoard);

        assertEquals(asBoard(expectedBoard), board.toString());
    }

    private String asBoard(String[] boardArray) {
        String board = "";
        String separator = "";
        for (int y = 0; y < boardArray.length; y++) {
            board += separator + boardArray[y];
            separator = "\n";
        }
        return board;
    }

    @Test
    public void isFree_occupied() {
        Board board = createBoard(
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|-x--------|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");

        assertFalse(board.isFree(1, 4));
    }

    @Test
    public void isFree() {
        Board board = createBoard(
                "|xxxxxxxxxx|",
                "|xxxxxxxxxx|",
                "|xxxxxxxxxx|",
                "|xxxxxxxxxx|",
                "|x-xxxxxxxx|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");

        assertTrue(board.isFree(1, 4));
    }

    @Test
    public void clearRows() {
        Board board = createBoard(
                "|----------|",
                "|----x-----|",
                "|xxxxxxxxxx|",
                "|xxxxxxxxxx|",
                "|-x--x----x|",
                "|xxxxxxxxxx|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");

        assertEquals(3, board.clearRows(2, 4));

        assertEquals(createBoard(
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----x-----|",
                "|-x--x----x|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ"), board);
    }

    @Test
    public void copy() {
        Board board = board();

        assertEquals(board, board.copy());
    }

    @Test
    public void restore() {
        Board empty = createBoard(8, 4);
        Board copy = board();
        empty.restore(copy);

        assertEquals(copy, empty);
    }

    private Board board() {
        return createBoard(
                "|x-------|",
                "|x-------|",
                "|xxx----x|",
                "|xxx-x-xx|",
                "ŻŻŻŻŻŻŻŻŻŻ");
    }
}

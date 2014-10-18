package com.github.tetrisanalyzer.board;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @Test(expected = IllegalArgumentException.class)
    public void tooLow() {
        Board.create(
                "|----------|",
                "|----x-----|",
                "|-x--x----x|",
                "¯¯¯¯¯¯¯¯¯¯¯¯");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooNarrow() {
        Board.create(
                "|---|",
                "|---|",
                "|---|",
                "|-x-|",
                "¯¯¯¯¯");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooWide() {
        new Board(33, 10);
    }

    @Test
    public void testToString() {
        String[] boardArray = new String[]{
                "|----------|",
                "|----------|",
                "|----x-----|",
                "|-x--x----x|",
                "¯¯¯¯¯¯¯¯¯¯¯¯"};

        Board board = Board.create(boardArray);

        assertEquals(board.toString(), asBoard(boardArray));
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
        Board board = Board.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|-x--------|",
                "¯¯¯¯¯¯¯¯¯¯¯¯");

        assertFalse(board.isFree(1, 4));
    }

    @Test
    public void isFree() {
        Board board = Board.create(
                "|xxxxxxxxxx|",
                "|xxxxxxxxxx|",
                "|xxxxxxxxxx|",
                "|xxxxxxxxxx|",
                "|x-xxxxxxxx|",
                "¯¯¯¯¯¯¯¯¯¯¯¯");

        assertTrue(board.isFree(1, 4));
    }

    @Test
    public void clearRows() {
        Board board = Board.create(
                "|----------|",
                "|----x-----|",
                "|xxxxxxxxxx|",
                "|xxxxxxxxxx|",
                "|-x--x----x|",
                "|xxxxxxxxxx|",
                "¯¯¯¯¯¯¯¯¯¯¯¯");

        assertEquals(3, board.clearRows(2, 4));

        assertEquals(Board.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----x-----|",
                "|-x--x----x|",
                "¯¯¯¯¯¯¯¯¯¯¯¯"), board);
    }

    @Test
    public void copy() {
        Board board = getBoard();

        assertEquals(board, board.copy());
    }

    @Test
    public void restore() {
        Board empty = new Board(8, 4);
        Board copy = getBoard();
        empty.restore(copy);

        assertEquals(copy, empty);
    }

    private Board getBoard() {
        return Board.create(
                "|x-------|",
                "|x-------|",
                "|xxx----x|",
                "|xxx-x-xx|",
                "¯¯¯¯¯¯¯¯¯¯");
    }
}

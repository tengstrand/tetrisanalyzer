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
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooNarrow() {
        Board.create(
                "|---|",
                "|---|",
                "|---|",
                "|-x-|",
                "ŻŻŻŻŻ");
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
                "ŻŻŻŻŻŻŻŻŻŻŻŻ"};

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
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");

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
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");

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
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");

        assertEquals(3, board.clearRows(2, 4));

        assertEquals(Board.create(
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
                "ŻŻŻŻŻŻŻŻŻŻ");
    }
}

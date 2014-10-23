package com.github.tetrisanalyzer.board;

import org.junit.Test;

import static com.github.tetrisanalyzer.board.Board.createBoard;
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

        Board board = Board.create(expectedBoard);

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
        Board board = Board.create(
                "|---------|",
                "|---------|",
                "|---------|",
                "|---------|",
                "|-x-------|",
                "ŻŻŻŻŻŻŻŻŻŻŻ");

        assertFalse(board.isFree(1, 4));
    }

    @Test
    public void isFree() {
        Board board = Board.create(
                "|xxxxxxxxx|",
                "|xxxxxxxxx|",
                "|xxxxxxxxx|",
                "|xxxxxxxxx|",
                "|x-xxxxxxx|",
                "ŻŻŻŻŻŻŻŻŻŻŻ");

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

    @Test
    public void cantHaveOddNumberOfCellsOnAnEvenBoardWidth() {
        try {
            Board.create(
                    "|------|",
                    "|------|",
                    "|x-----|",
                    "|xx----|",
                    "ŻŻŻŻŻŻŻŻ");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void canHaveOddNumberOfCellsOnAnOddBoardWidth() {
        Board.create(
                "|-----|",
                "|-----|",
                "|x----|",
                "|xx---|",
                "ŻŻŻŻŻŻŻ");
    }

    private Board board() {
        return Board.create(
                "|x-------|",
                "|x-------|",
                "|xxx----x|",
                "|xxx-x-xx|",
                "ŻŻŻŻŻŻŻŻŻŻ");
    }
}

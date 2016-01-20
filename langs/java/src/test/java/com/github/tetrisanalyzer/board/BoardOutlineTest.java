package com.github.tetrisanalyzer.board;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardOutlineTest {
    private Board board;

    @Before
    public void setUp() {
        board = Board.create(
                "|------x|",
                "|-----xx|",
                "|-x---xx|",
                "|x-x--xx|",
                "---------");
    }

    @Test
    public void getOutline() {
        assertEquals(new BoardOutline(0, 4, 3, 2, 3, 4, 4, 1, 0, 0), new BoardOutline(board));
    }

    @Test
    public void getOutline_withLeftWall() {
        assertEquals(new BoardOutline(0, 4, 0, 3, 2, 3, 4, 4, 1, 0, 0), new BoardOutline(board, true));
    }

    @Test
    public void minY() {
        assertEquals(0, new BoardOutline(board).minY);
    }

    @Test
    public void freeRows() {
        assertEquals(2.42, new BoardOutline(board).freeRows, 0.01);
    }
}

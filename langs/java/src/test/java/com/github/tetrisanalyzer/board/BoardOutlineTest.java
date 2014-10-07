package com.github.tetrisanalyzer.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BoardOutlineTest {
    private Board board;

    @Before
    public void setUp() {
        board = Board.create(
                "#------x#",
                "#-----xx#",
                "#-x---xx#",
                "#x-x--xx#",
                "#########");
    }

    @Test
    public void getOutline() {
        Assert.assertEquals(new BoardOutline(0, 3, 2, 3, 4, 4, 1, 0, 0), new BoardOutline(board));
    }

    @Test
    public void minY() {
        Assert.assertEquals(0, new BoardOutline(board).getMinY());
    }
}

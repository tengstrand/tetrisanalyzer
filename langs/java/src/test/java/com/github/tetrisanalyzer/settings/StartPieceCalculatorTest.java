package com.github.tetrisanalyzer.settings;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StartPieceCalculatorTest {

    @Test
    public void calculateStartPosForBoardWidthTen() {
        for (int x=6; x>= 0; x--) {
            assertEquals(x, StartPieceCalculator.startX(10, x));
        }
    }

    @Test
    public void calc() {
        assertEquals(7, StartPieceCalculator.startX(15, 4));
        assertEquals(6, StartPieceCalculator.startX(14, 4));
        assertEquals(5, StartPieceCalculator.startX(13, 4));
        assertEquals(5, StartPieceCalculator.startX(12, 4));
        assertEquals(4, StartPieceCalculator.startX(11, 4));
        assertEquals(4, StartPieceCalculator.startX(10, 4));
        assertEquals(3, StartPieceCalculator.startX(9, 4));
        assertEquals(2, StartPieceCalculator.startX(8, 4));
        assertEquals(2, StartPieceCalculator.startX(7, 4));
        assertEquals(1, StartPieceCalculator.startX(6, 4));
        assertEquals(0, StartPieceCalculator.startX(5, 4));
        assertEquals(0, StartPieceCalculator.startX(4, 4));
    }
}

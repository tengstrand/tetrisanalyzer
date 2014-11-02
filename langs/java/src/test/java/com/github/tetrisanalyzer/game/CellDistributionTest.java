package com.github.tetrisanalyzer.game;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class CellDistributionTest {

    @Test
    public void fillDistribution() {
        CellDistribution cellDistribution = new CellDistribution(6, 5);
        cellDistribution.cells[4] = 2;
        cellDistribution.cells[6] = 4;
        cellDistribution.cells[8] = 10;

        double[] filled = cellDistribution.fillDistribution();

        double[] expected = new double[] { 0, 0, 0.1, 0.3, 1, 0, 0, 0, 0, 0, 0, 0, 0 };

        assertArrayEquals(expected, filled, 0.00001);
    }
}

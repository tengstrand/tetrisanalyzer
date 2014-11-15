package com.github.tetrisanalyzer.game;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.tetrisanalyzer.game.XY.XY;
import static junit.framework.Assert.assertEquals;

public class DistributionTest {

    @Test
    public void coordinates() {
        List<Integer> cells = Arrays.asList(50, 100, 120, 70);
        Distribution distribution = new Distribution(cells);
        List<XY> lines = distribution.coordinates(10, 1000, 200, 100);
        List<XY> expected = Arrays.asList(XY(10, 1100), XY(76, 1029), XY(143, 1000), XY(210, 1072));

        assertEquals(expected, lines);
    }
}

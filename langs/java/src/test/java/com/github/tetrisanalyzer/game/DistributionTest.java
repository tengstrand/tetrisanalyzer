package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.gui.Vertex;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.tetrisanalyzer.gui.Vertex.Vertex;
import static junit.framework.Assert.assertEquals;

public class DistributionTest {

    @Test
    public void vertices() {
        List<Integer> cells = Arrays.asList(50, 100, 120, 70);
        Distribution distribution = new Distribution(cells);
        List<Vertex> lines = distribution.coordinates(10, 1000, 200, 100);
        List<Vertex> expected = Arrays.asList(Vertex(10, 1100), Vertex(76, 1029), Vertex(143, 1000), Vertex(210, 1072));

        assertEquals(expected, lines);
    }
}

package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.gui.Lines;
import com.github.tetrisanalyzer.gui.Vertex;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class DistributionTest {

    @Test
    public void clipHorizontal() {
        List<Integer> cells = Arrays.asList(500, 1000, 1200, 700);
        Distribution distribution = new Distribution(cells);
        List<Vertex> lines = distribution.verticesClipHorizontal(0, 1, 200);

        List<Vertex> expected = Arrays.asList(
                new Vertex(0, 500),
                new Vertex(66.66666666666667, 1000),
                new Vertex(133.33333333333334, 1200.0),
                new Vertex(200, 700));

        assertEquals(expected, lines);
    }

    @Test
    public void clipHorizontal_ends_fraction() {
        List<Integer> cells = Arrays.asList(500, 1000, 1200, 700);
        Distribution distribution = new Distribution(cells);
        List<Vertex> lines = distribution.verticesClipHorizontal(0.1, 0.6, 200);
        List<Vertex> expected = Arrays.asList(
                new Vertex(0.0, 700.0),
                new Vertex(100.0, 1000.0),
                new Vertex(200, 1079.0));

        assertEquals(expected, lines);
    }

    @Test
    public void clipHorizontal_one_line_fraction() {
        List<Integer> cells = Arrays.asList(500, 1000, 1200, 700);
        Distribution distribution = new Distribution(cells);
        List<Vertex> lines = distribution.verticesClipHorizontal(0.5, 0.6, 200);
        List<Vertex> expected = Arrays.asList(
                new Vertex(0.0, 500.0),
                new Vertex(200.0, 1079.0));

        assertEquals(expected, lines);
    }

    @Test
    public void test() {
        List<Vertex> vertices = Arrays.asList(
                new Vertex(0.0, 400.0),
                new Vertex(100.0, 1000.0),
                new Vertex(200, 1079.0),
                new Vertex(300, 500.0));
        Distribution distribution = new Distribution(Arrays.asList(0));

        Lines result = distribution.clipVertically(0.2, 0.5, 100, vertices);

        int xx = 1;
    }
}

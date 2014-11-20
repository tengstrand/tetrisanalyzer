package com.github.tetrisanalyzer.gui;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class VerticesTest {

    @Test
    public void normalizeY() {
        Vertices vertices = new Vertices(
                new Vertex(0.0, 500.0),
                new Vertex(1.0, 1000.0),
                new Vertex(2.0, 1200.0),
                new Vertex(3.0, 700.0));

        Vertices normalized = vertices.normalizeY();

        Vertices expected = new Vertices(
                new Vertex(0.0, 0.4166666666666667),
                new Vertex(1.0, 0.8333333333333334),
                new Vertex(2.0, 1.0),
                new Vertex(3.0, 0.5833333333333334));

        assertEquals(expected, normalized);
    }
}

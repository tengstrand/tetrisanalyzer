package com.github.tetrisanalyzer.gui;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class VerticesTest {

	private Vertices vertices = new Vertices(
			new Vertex(0.0, 0.417),
			new Vertex(1.0, 0.833),
			new Vertex(2.0, 1.0),
			new Vertex(3.0, 0.583));


	@Test
    public void normalizeY() {
        Vertices vertices = new Vertices(
                new Vertex(0.0, 500.0),
                new Vertex(1.0, 1000.0),
                new Vertex(2.0, 1200.0),
                new Vertex(3.0, 700.0));

        Vertices normalized = vertices.normalizeY(1);

        Vertices expected = new Vertices(
                new Vertex(0.0, 0.5833333333333333),
                new Vertex(1.0, 0.16666666666666663),
                new Vertex(2.0, 0.0),
                new Vertex(3.0, 0.41666666666666663));

        assertEquals(expected, normalized);
    }

	@Test
	public void clipHorizontal_onBothSides_withLinesBetween() {
		Vertices result = vertices.clipHorizontal(0.4, 0.9);

		Vertices expected = new Vertices(
				new Vertex(1.2000000000000002, 0.8664),
				new Vertex(2.0, 1.0),
				new Vertex(2.7, 0.7081));

		assertEquals(expected, result);
	}

	@Test
	public void clipHorizontal_onBothSides_oneLine() {
		Vertices result = vertices.clipHorizontal(0.2, 0.22);

		Vertices expected = new Vertices(
				new Vertex(0.6000000000000001, 0.6666),
				new Vertex(0.66, 0.69156));

		assertEquals(expected, result);
	}

	@Test
	public void clipHorizontal_onlyLeftSide() {
		Vertices result = vertices.clipHorizontal(0.4, 1);

		Vertices expected = new Vertices(
				new Vertex(1.2000000000000002, 0.8664),
				new Vertex(2.0, 1.0),
				new Vertex(3.0, 0.583));

		assertEquals(expected, result);
	}

	@Test
	public void clipHorizontal_onlyRightSide() {
		Vertices result = vertices.clipHorizontal(0, 0.9);

		Vertices expected = new Vertices(
				new Vertex(0.0, 0.417),
				new Vertex(1.0, 0.833),
				new Vertex(2.0, 1.0),
				new Vertex(2.7, 0.7081));

		assertEquals(expected, result);
	}

    @Test
    public void clipVertically_top() {
        Lines lines = vertices.clipVertically(0, 0.9);

        Lines expected = new Lines(
                new Line(new Vertex(0.0, 0.417), new Vertex(1.0,0.833)),
                new Line(new Vertex(1.0, 0.833), new Vertex(1.4011976047904193, 0.9)),
                new Line(new Vertex(2.239808153477218, 0.9), new Vertex(3.0,0.583)));

        assertEquals(expected, lines);
    }

    @Test
    public void clipVertically_top_fromTopLeft() {
        Vertices vertices = new Vertices(
                new Vertex(0.0, 0.0),
                new Vertex(1.0, 1.0));

        Lines lines = vertices.clipVertically(0, 0.9);

        Lines expected = new Lines(
                new Line(new Vertex(0.0, 0.0), new Vertex(0.9,0.9)));

        assertEquals(expected, lines);
    }

    @Test
    public void clipVertically_top_fromBottomLeft() {
        Vertices vertices = new Vertices(
                new Vertex(0.0, 1.0),
                new Vertex(1.0, 0.0));

        Lines lines = vertices.clipVertically(0.0, 0.9);

        Lines expected = new Lines(
                new Line(new Vertex(0.09999999999999998, 0.9), new Vertex(1,0.0)));

        assertEquals(expected, lines);
    }

    @Test
    public void clipVertically_bottom() {
        Lines lines = vertices.clipVertically(0.45, 1);

        Lines expected = new Lines(
                new Line(new Vertex(0.07932692307692316, 0.45), new Vertex(1.0,0.833)),
                new Line(new Vertex(1.0, 0.833), new Vertex(2.0, 1.0)),
                new Line(new Vertex(2.0, 1.0), new Vertex(3.0,0.583)));

        assertEquals(expected, lines);
    }
}

package com.github.tetrisanalyzer.gui;

public class Line {
    public final Vertex vertex1;
    public final Vertex vertex2;

    public static Line Line(Vertex vertex1, Vertex vertex2) {
        return new Line(vertex1, vertex2);
    }

    private Line(Vertex vertex1, Vertex vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        if (!vertex1.equals(line.vertex1)) return false;
        if (!vertex2.equals(line.vertex2)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = vertex1.hashCode();
        result = 31 * result + vertex2.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return vertex1 + "-" + vertex2;
    }
}

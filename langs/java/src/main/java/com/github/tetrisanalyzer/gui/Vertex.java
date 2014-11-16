package com.github.tetrisanalyzer.gui;

public class Vertex {
    public int x;
    public int y;

    public static Vertex Vertex(int x, int y) {
        return new Vertex(x, y);
    }

    private Vertex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        if (x != vertex.x) return false;
        if (y != vertex.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

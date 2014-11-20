package com.github.tetrisanalyzer.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vertices {
    List<Vertex> vertices;

    public Vertices(Vertex... vertices) {
        this.vertices = new ArrayList<>(vertices.length);
        this.vertices.addAll(Arrays.asList(vertices));
    }

    public Vertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    private double maxY() {
        double max = Double.MIN_VALUE;

        for (Vertex vertex : vertices) {
            if (vertex.y > max) {
                max = vertex.y;
            }
        }
        return max;
    }

    public Vertices normalizeY() {
        List<Vertex> result = new ArrayList<>(vertices.size());

        double maxy = maxY();
        for (Vertex vertex : vertices) {
            result.add(new Vertex(vertex.x, vertex.y / maxy));
        }
        return new Vertices(result);
    }

    public Vertices clipHorizontal(double wx1, double wx2) {
        return null;
    }

    public Lines clipVertically(double wy1, double wy2) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertices vertices1 = (Vertices) o;

        if (vertices != null ? !vertices.equals(vertices1.vertices) : vertices1.vertices != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return vertices != null ? vertices.hashCode() : 0;
    }

    @Override
    public String toString() {
        return vertices.toString();
    }
}

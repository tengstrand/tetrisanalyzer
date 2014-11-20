package com.github.tetrisanalyzer.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lines implements Iterable<Line> {
    public final List<Line> lines;

    public static Lines fromLines(List<Line> lines) {
        return new Lines(lines);
    }

    public static Lines fromVertices(List<Vertex> vertices) {
        List<Line> lines = new ArrayList<>();

        Vertex vertex1 = vertices.get(0);

        for (int i=1; i<vertices.size(); i++) {
            Vertex vertex2 = vertices.get(i);
            lines.add(new Line(vertex1, vertex2));
            vertex1 = vertex2;
        }
        return new Lines(lines);
    }

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public Iterator<Line> iterator() {
        return lines.iterator();
    }

    public void drawLines(int x, int y, Graphics g) {
        for (Line line : lines) {
            g.drawLine(x + (int)line.vertex1.x, y + (int)line.vertex1.y, x + (int)line.vertex2.x, y + (int)line.vertex2.y);
        }
    }

    public void drawSelection(int x, int y, int startIdx, int endIdx, Graphics g) {
        int height = maxHeight(startIdx, endIdx);

        for (int i=startIdx; i < endIdx; i++) {
            g.fillPolygon(polygon(x, y, height, lines.get(i)));
        }
    }

    private int maxHeight(int startIdx, int endIdx) {
        /*
        int height = Integer.MIN_VALUE;

        for (int i=startIdx; i < endIdx; i++) {
            if (lines.get(i).maxY() > height) {
                height = lines.get(i).maxY();
            }
        }
        return height;
        */
        return 0;
    }


    private Polygon polygon(int x, int y, int height, Line line) {
        /*
        int[] xpoints = new int[4];
        int[] ypoints = new int[4];

        xpoints[0] = line.vertex1.x + x;
        xpoints[1] = line.vertex2.x + x;
        xpoints[2] = line.vertex2.x + x;
        xpoints[3] = line.vertex1.x + x;

        ypoints[0] = line.vertex1.y + y;
        ypoints[1] = line.vertex2.y + y;
        ypoints[2] = height + y;
        ypoints[3] = height + y;

        return new Polygon(xpoints, ypoints, 4);
        */
        return null;
    }

    public Lines zoom(int width, int height) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lines lines1 = (Lines) o;

        if (lines != null ? !lines.equals(lines1.lines) : lines1.lines != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return lines != null ? lines.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Lines{" +
                "lines=" + lines +
                '}';
    }
}

package com.github.tetrisanalyzer.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Lines implements Iterable<Line> {
    public final List<Line> lines;

    public Lines(Line... lines) {
        this.lines = Arrays.asList(lines);
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

    public void drawColorredLines(int x, int y, Color[] colors, Graphics g) {
        int i= 0;
        for (Line line : lines) {
            g.setColor(colors[i++]);
            g.drawLine(x + (int)line.vertex1.x, y + (int)line.vertex1.y, x + (int)line.vertex2.x, y + (int)line.vertex2.y);
        }
    }

    public Lines resize(double wx1, double wy1, double wx2, double wy2, int width, int height) {
        List<Line> result = new ArrayList<>();

        for (Line line : lines) {
            result.add(line.resize(wx1, wy1, wx2, wy2, width, height));
        }
        return new Lines(result);
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

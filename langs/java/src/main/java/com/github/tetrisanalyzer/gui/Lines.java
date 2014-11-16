package com.github.tetrisanalyzer.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.tetrisanalyzer.gui.Line.Line;

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
            lines.add(Line(vertex1, vertex2));
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

    public void dras(Graphics g) {
        for (Line line : lines) {
            g.drawLine(line.vertex1.x, line.vertex1.y, line.vertex2.x, line.vertex2.y);
        }
    }
}

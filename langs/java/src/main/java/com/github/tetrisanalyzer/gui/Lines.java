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

    public void draw(int x, int y, Graphics g) {
        for (Line line : lines) {
            g.drawLine(x + line.vertex1.x, y + line.vertex1.y, line.vertex2.x + x, line.vertex2.y + y);
        }
    }

    public void draw(int x, int y, int startIdx, int endIdx, int height, Graphics g) {
        int idx = 0;
        for (Line line : lines) {
            if (idx >= startIdx && idx <= endIdx) {
                g.fillPolygon(polygon(x, y, height, lines.get(idx)));
            } else {
                g.drawLine(x + line.vertex1.x, y + line.vertex1.y, line.vertex2.x + x, line.vertex2.y + y);
            }
            idx++;
        }
    }

    private Polygon polygon(int x, int y, int height, Line line) {
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
    }
}

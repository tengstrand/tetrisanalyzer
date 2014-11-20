package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.gui.Line;
import com.github.tetrisanalyzer.gui.Lines;
import com.github.tetrisanalyzer.gui.Vertex;
import com.github.tetrisanalyzer.gui.Vertices;

import java.util.ArrayList;
import java.util.List;

public class Distribution {
    public int[] cells;

    public Distribution(int boardWidth, int boardHeight) {
        cells = new int[((boardWidth-1) * boardHeight) / 2 + 1];
    }

    public Distribution(List<Integer> cells) {
        this.cells = new int[cells.size()];

        int i = 0;
        for (Integer square : cells) {
            if (i == cells.size()) {
                break;
            }
            this.cells[i++] = square;
        }
    }

    public void increaseArea(int numberOfCells) {
        int index = numberOfCells >> 1;
        cells[index] += numberOfCells;
    }

    /*
        private long min(double left, double right) {
            long min = Long.MAX_VALUE;
            for (int i=startIdx; i<= endIdx; i++) {
                if (cells[i] < min) {
                    min = cells[i];
                }
            }
            return min;
        }

        private long max(int startIdx, int endIdx) {
            long max = Long.MIN_VALUE;
            for (int i=startIdx; i<= endIdx; i++) {
                if (cells[i] > max) {
                    max = cells[i];
                }
            }
            return max;
        }
    */

    Vertices toVertices() {
        List<Vertex> vertices = new ArrayList<>();

        for (int x=0; x<cells.length; x++) {
            vertices.add(new Vertex(x, cells[x]));
        }
        return new Vertices(vertices);
    }

    public Lines lines(double wx1, double wx2, double wy1, double wy2, int width, int height) {
        return toVertices().normalizeY().clipHorizontal(wx1, wx2).clipVertically(wy1, wy2).zoom(width, height);
    }

    List<Vertex> verticesClipHorizontal(double wx1, double wx2, int width) {
        int length = cells.length;

        double x1 = length * wx1;
        double x2 = length * wx2;
        int index1 = (int)x1;
        int index2;
        double slice1 = x1 - index1;
        double slice2;
        if (wx2 == 1) {
            index2 = length - 1;
            slice2 = 1;
        } else {
            index2 = (int)x2;
            slice2 = x2 - index2;
        }

        int idx = index2 - index1;
        double dx = idx > 0 ? (double)width / idx : 0;

        List<Vertex> vertices = new ArrayList<>(cells.length + 1);

        int y1 = cells[0] + (int)((cells[1] - cells[0]) * slice1);
        int y2 = cells[index2 - 1] + (int)((cells[index2] - cells[index2 - 1]) * slice2);

        vertices.add(new Vertex(0, y1));

        for (int i=index1+1; i<index2; i++) {
            vertices.add(new Vertex(i * dx, cells[i]));
        }
        vertices.add(new Vertex(width, y2));

        return vertices;
    }

    Lines clipVertically(double wy1, double wy2, int height, List<Vertex> vertices) {
        double maxy = Double.MIN_VALUE;

        for (Vertex vertex : vertices) {
            if (vertex.y > maxy) {
                maxy = vertex.y;
            }
        }
        double topy = maxy * (1 - wy1);
        double bottomy = maxy * (1 - wy2);

        List<Line> lines = new ArrayList<>(vertices.size());

        if (vertices.size() <= 1) {
            return Lines.fromLines(lines);
        }
        Vertex v1 = vertices.get(0);
        for (int i=1; i<vertices.size(); i++) {
            Vertex v2 = vertices.get(i);
            double min = Math.min(v1.y, v2.y);
            double max = (Math.max(v1.y, v2.y));

            if ((min > topy && max > topy) || (min < bottomy && max < bottomy)) {
                continue;
            }
            double x1 = v1.x;
            double y1 = v1.y;
            if (v1.y > topy) {
                x1 = v1.x + (v2.x - v1.x) * (topy / v1.y);
                y1 = topy;
            }
            if (v1.y < bottomy) {
                x1 = v1.x + (v2.x - v1.x) * ((bottomy / v1.y) - 1);
                y1 = bottomy;
            }
            double x2 = v2.x;
            double y2 = v2.y;
            if (v2.y > topy) {
                x2 = v1.x + (v2.x - v1.x) * (topy / v2.y);
                y2 = topy;
            }
            if (v2.y < bottomy) {
                x1 = v1.x + (v2.x - v1.x) * (bottomy / v1.y);
                y1 = bottomy;
            }
            lines.add(new Line(new Vertex(x1,y1), new Vertex(x2,y2)));
            v1 = v2;
        }
        return Lines.fromLines(lines);
    }

    public List<Vertex> vertices(double left, double right, int width, int height) {

/*
        double dx = (double)width / (endIdx - startIdx);
        double dy = (double)height / (max - min);

        for (int i = startIdx; i<=endIdx; i++) {
            int x = (int)((i - startIdx) * dx);
            int y = height + - (int)((cells[i] - min) * dy);
            result.add(Vertex(x, y));
        }*/
        return null;
    }

    private double sliceLeft(int x1, int x2, double slice) {
        int xx = 1;

        return 0;
    }

    public Distribution copy() {
        throw new IllegalStateException("Not implemented");
    }
}

package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.gui.Vertex;
import com.github.tetrisanalyzer.gui.Vertices;

import java.util.ArrayList;
import java.util.List;

public class Distribution {
    private int areaIndex = 0;
    public final int boardWidth;
    public final int boardHeight;
    public final long[] cells;

    private int shift;

    public Distribution(int boardWidth, int boardHeight) {
        setShift(boardWidth);
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        cells = new long[(((boardWidth-1) * boardHeight) >> shift) + 1];
        setAreaIndex();
    }

    public Distribution(int boardWidth, int boardHeight, List<Long> cells) {
        setShift(boardWidth);
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.cells = new long[cells.size()];
        setAreaIndex();

        int i = 0;
        for (long cell : cells) {
            if (i == cells.size()) {
                break;
            }
            this.cells[i++] = cell;
        }
    }

    private void setShift(int boardWidth) {
        shift = (boardWidth % 4 == 0) ? 2 : 1;
    }

    private void setAreaIndex() {
        setAreaPercentage(30);
    }

    public void setAreaPercentage(int percentage) {
        areaIndex = cells.length - (int)(cells.length * (percentage / 100.0));
        if (areaIndex < 0) {
            areaIndex = 0;
        } else if (areaIndex >= cells.length) {
            areaIndex = cells.length - 1;
        }
    }

    public void increaseArea(int numberOfCells) {
        int index = numberOfCells >> shift;
        cells[index] += (numberOfCells == 0 ? 1 : numberOfCells);
        setAreaIndex();
    }

    public Vertices toVertices() {
        List<Vertex> vertices = new ArrayList<>();

        double dx = 1.0 / (cells.length - 1);
        for (int i=0; i<cells.length; i++) {
            vertices.add(new Vertex(i * dx, cells[i]));
        }
        return new Vertices(vertices);
    }

    public Distribution copy() {
        throw new IllegalStateException("Not implemented");
    }

    public String export() {
        String result = "[";
        String separator = "";
        for (long cell : cells) {
            result += separator + cell;
            separator = ",";
        }
        return result + "]";
    }

    public double area() {
        long total = 0;
        for (int i=0; i<cells.length; i++) {
            total += cells[i];
        }
        long area = 0;
        for (int i=areaIndex; i<cells.length; i++) {
            area += cells[i];
        }
        return (double)area / total * 100.0;
    }
}

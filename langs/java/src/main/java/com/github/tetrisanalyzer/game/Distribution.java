package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.gui.Vertex;
import com.github.tetrisanalyzer.gui.Vertices;

import java.util.ArrayList;
import java.util.List;

public class Distribution {
    public long[] cells;

    public Distribution(int boardWidth, int boardHeight) {
        cells = new long[((boardWidth-1) * boardHeight) / 2 + 1];
    }

    public Distribution(List<Long> cells) {
        this.cells = new long[cells.size()];

        int i = 0;
        for (long cell : cells) {
            if (i == cells.size()) {
                break;
            }
            this.cells[i++] = cell;
        }
    }

    public void increaseArea(int numberOfCells) {
        int index = numberOfCells >> 1;
        cells[index] += numberOfCells;
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
}

package com.github.tetrisanalyzer.game;

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

    Vertices toVertices() {
        List<Vertex> vertices = new ArrayList<>();

        double dx = 1.0 / (cells.length - 1);
        for (int i=0; i<cells.length; i++) {
            vertices.add(new Vertex(i * dx, cells[i]));
        }
        return new Vertices(vertices);
    }

    public Lines lines(double wx1, double wy1, double wx2, double wy2, int width, int height) {
        return toVertices().normalizeY().clipHorizontal(wx1, wx2).clipVertically(wy1, wy2).resize(wx1, wy1, wx2, wy2, width, height);
    }

    public Distribution copy() {
        throw new IllegalStateException("Not implemented");
    }

    public String export() {
        String result = "[";
        String separator = "";
        for (int cell : cells) {
            result += separator + cell;
            separator = ",";
        }
        return result + "]";
    }
}

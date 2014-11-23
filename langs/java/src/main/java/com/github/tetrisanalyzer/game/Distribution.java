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

        for (int x=0; x<cells.length; x++) {
            vertices.add(new Vertex(x, cells[x]));
        }
        return new Vertices(vertices);
    }

    public Lines lines(double wx1, double wy1, double wx2, double wy2, int width, int height) {
        double dy = ((wx2 - wx1) / (wy2-wy1)) / (width / height);

        return toVertices().normalizeY(dy).clipHorizontal(wx1, wx2).normalizeX().clipVertically(wy1, wy2).resize(width, height);
    }

    public Distribution copy() {
        throw new IllegalStateException("Not implemented");
    }
}

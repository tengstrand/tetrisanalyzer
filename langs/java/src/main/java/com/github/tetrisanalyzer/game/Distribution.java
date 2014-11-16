package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.gui.Lines;
import com.github.tetrisanalyzer.gui.Vertex;

import java.util.ArrayList;
import java.util.List;

import static com.github.tetrisanalyzer.gui.Vertex.Vertex;

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

    private long min(int startIdx, int endIdx) {
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

    public Lines lines(int startIdx, int endIdx, int width, int height) {
        return Lines.fromVertices(vertices(startIdx, endIdx, width, height));
    }

    public List<Vertex> vertices(int startIdx, int endIdx, int width, int height) {
        long min = min(startIdx, endIdx);
        long max = max(startIdx, endIdx);

        List<Vertex> result = new ArrayList<>(cells.length);

        double dx = (double)width / (endIdx - startIdx);
        double dy = (double)height / (max - min);

        for (int i = startIdx; i<=endIdx; i++) {
            int x = (int)((i - startIdx) * dx);
            int y = height + - (int)((cells[i] - min) * dy);
            result.add(Vertex(x, y));
        }
        return result;
    }

    public Distribution copy() {
        throw new IllegalStateException("Not implemented");
    }
}

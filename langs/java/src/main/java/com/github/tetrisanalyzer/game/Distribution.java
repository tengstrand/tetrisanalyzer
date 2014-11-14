package com.github.tetrisanalyzer.game;

import java.util.List;

public class Distribution {
    public int[] cells;
    public long area;
    public int startIdx;
    public int endIdx;

    public Distribution(int boardWidth, int boardHeight) {
        cells = new int[((boardWidth-1) * boardHeight) / 2 + 1];
        startIdx = 0;
        endIdx = cells.length - 1;
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
        startIdx = 0;
        endIdx = this.cells.length - 1;

        initArea();
    }

    private void initArea() {
        int sum = 0;

        for (int i=startIdx; i<=endIdx; i++) {
            sum += cells[i];
        }
        area = sum;
    }

    public void addCells(int numberOfCells) {
        int index = numberOfCells >> 1;
        cells[index] += numberOfCells;

        if (index >= startIdx && index <= endIdx) {
            area += numberOfCells;
        }
    }

    public Distribution copy() {
        throw new IllegalStateException("Not implemented");
    }
}

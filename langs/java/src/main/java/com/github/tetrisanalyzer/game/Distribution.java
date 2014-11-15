package com.github.tetrisanalyzer.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.github.tetrisanalyzer.game.XY.XY;

public class Distribution {
    public int[] cells;
    public long area;
    public long totalArea;
    public int startIdx;
    public int endIdx;

    public Distribution(int boardWidth, int boardHeight) {
        cells = new int[((boardWidth-1) * boardHeight) / 2 + 1];
        startIdx = (int)(cells.length * 0.1);
        endIdx = (int)(cells.length * 0.15);
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

    public String percentArea() {
        return String.format(Locale.ENGLISH, "%.4f", (area / (double)totalArea) * 100);
    }

    public void initArea() {
        area = 0;
        totalArea = 0;

        for (int i=startIdx; i<=endIdx; i++) {
            area += cells[i];
        }
        for (int i=0; i<cells.length; i++) {
            totalArea += cells[i];
        }
    }

    public void increaseArea(int numberOfCells) {
        int index = numberOfCells >> 1;
        cells[index] += numberOfCells;

        if (index >= startIdx && index <= endIdx) {
            area += numberOfCells;
        }
        totalArea += numberOfCells;
    }

    private long min() {
        long min = Long.MAX_VALUE;
        for (int i=startIdx; i<= endIdx; i++) {
            if (cells[i] < min) {
                min = cells[i];
            }
        }
        return min;
    }

    private long max() {
        long max = Long.MIN_VALUE;
        for (int i=startIdx; i<= endIdx; i++) {
            if (cells[i] > max) {
                max = cells[i];
            }
        }
        return max;
    }

    public List<XY> coordinates(int x0, int y0, int width, int height) {
        long min = min();
        long max = max();

        List<XY> result = new ArrayList<>(cells.length);

        double dx = (double)width / (endIdx - startIdx);
        double dy = (double)height / (max - min);

        for (int i = startIdx; i<=endIdx; i++) {
            int x = (int)(x0 + (i - startIdx) * dx);
            int y = y0 + height + - (int)((cells[i] - min) * dy);
            result.add(XY(x, y));
        }
        return result;
    }


    public Distribution copy() {
        throw new IllegalStateException("Not implemented");
    }
}

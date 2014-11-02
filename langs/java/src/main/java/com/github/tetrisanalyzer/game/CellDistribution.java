package com.github.tetrisanalyzer.game;

public class CellDistribution {
    public long numberOfcells;
    public final int step;
    public final long[] cells;

    public CellDistribution(int boardWidth, int boardHeight) {
        numberOfcells = 0;
        step = 2 - (boardWidth & 1);
        cells = new long[(boardWidth - 1) * boardHeight + 1];
    }

    public CellDistribution(CellDistribution that) {
        numberOfcells = that.numberOfcells;
        step = that.step;
        cells = new long[that.cells.length];
        System.arraycopy(that.cells, 0, cells, 0, cells.length);
    }

    public String distribution() {
        String result = "";
        String separator = "";

        for (int i = 0; i< cells.length; i+=step) {
            result += separator + cells[i];
            separator = ",";
        }
        return result;
    }

    public double maxValue() {
        long max = 0;
        for (int i = 0; i<cells.length; i+=step) {
            long squares = cells[i] * i;

            if (squares > max) {
                max = squares;
            }
        }
        return max;
    }

    public int y(double maxValue, long amount, int height) {
        return height - (int)((amount / maxValue) * height);
    }
}

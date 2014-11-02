package com.github.tetrisanalyzer.game;

public class CellDistribution {
    public long numberOfcells;
    public final int step;
    public final long[] cells;

    public CellDistribution(long numberOfcells, int step, long[] cells) {
        this.numberOfcells = numberOfcells;
        this.step = step;
        this.cells = new long[cells.length];
        System.arraycopy(this.cells, 0, cells, 0, cells.length);
    }

    public CellDistribution(int boardWidth, int boardHeight) {
        numberOfcells = 0;
        step = 2 - (boardWidth & 1);
        cells = new long[(boardWidth - 1) * boardHeight + 1];
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

    public double[] fillDistribution() {
        int length = cells.length / step;
        double max = Long.MIN_VALUE;

        for (int i = 0; i< cells.length; i+=step) {
            long squares = cells[i] * i;
            if (squares > max) {
                max = squares;
            }
        }

        int j = 0;
        double[] result = new double[length];

        for (int i = 0; i< cells.length; i+=step) {
            result[j++] = (cells[i] * i) / max;
        }
        return result;
    }
}

package com.github.tetrisanalyzer.board;

import java.util.Arrays;

public class BoardOutline {
    public int minY;
    public int maxY;
    public double freeRows;
    private int[] outline;

    public BoardOutline(Board board) {
        this(board, false);
    }

    public BoardOutline(Board board, boolean withLeftWall) {
        int wall = withLeftWall ? 1 : 0;
        outline = new int[board.width + 1 + wall];
        minY = Integer.MAX_VALUE;
        maxY = Integer.MIN_VALUE;

        int freeCells = 0;
        for (int x = 0; x < board.width; x++) {
            int y = 0;
            while (board.isFree(x, y)) {
                freeCells++;
                y++;
            }
            outline[x + wall] = y;

            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }
        if (withLeftWall) {
            outline[0] = 0;
        }
        outline[board.width + wall] = 0;

        freeRows = ((double)freeCells) / board.width;
    }

    public BoardOutline(int minY, int maxY, int... outline) {
        this.minY = minY;
        this.maxY = maxY;
        this.outline = outline;
    }

    public int size() {
        return outline.length;
    }

    public int get(int x) {
        return outline[x];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardOutline that = (BoardOutline) o;

        if (minY != that.minY) return false;
        if (!Arrays.equals(outline, that.outline)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = minY;
        result = 31 * result + (outline != null ? Arrays.hashCode(outline) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BoardOutline{" +
                "minY=" + minY +
                "maxY=" + maxY +
                ", outline=" + outline +
                '}';
    }
}

package com.github.tetrisanalyzer.board;

import java.util.Arrays;

public class BoardOutline {
    public int minY;
    private int[] outline;

    public BoardOutline(Board board) {
        int[] outline = new int[board.width + 1];
        int minY = Integer.MAX_VALUE;

        for (int x = 0; x < board.width; x++) {
            int y = 0;
            while (board.isFree(x, y)) {
                y++;
            }
            outline[x] = y;

            if (y < minY) {
                minY = y;
            }
        }
        outline[board.width] = 0;

        this.outline = outline;
        this.minY = minY;
    }

    public BoardOutline(int minY, int... outline) {
        this.minY = minY;
        this.outline = outline;
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
                ", outline=" + outline +
                '}';
    }
}

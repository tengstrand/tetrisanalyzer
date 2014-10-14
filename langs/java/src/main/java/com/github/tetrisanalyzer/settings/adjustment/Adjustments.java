package com.github.tetrisanalyzer.settings.adjustment;

import java.util.ArrayList;
import java.util.List;

public class Adjustments {
    public final String piece;
    public final int startX;
    public final List<Integer> dx;
    public final List<Integer> dy;
    private final List<AdjustmentDxDy> source;

    public Adjustments(String piece, List<AdjustmentDxDy> source) {
        this.piece = piece;
        startX = 0;
        dx = new ArrayList<>();
        dy = new ArrayList<>();
        this.source = source;
    }

    public Adjustments(String piece, int startX, List<Integer> dx, List<Integer> dy, List<AdjustmentDxDy> source) {
        this.piece = piece;
        this.startX = startX;
        this.dx = dx;
        this.dy = dy;
        this.source = source;
    }

    public String export() {
        String result = piece + ": [";
        String separator = "";

        for (AdjustmentDxDy adjustment : source) {
            result += separator + "[" + adjustment.dx + "," + adjustment.dy + "]";
            separator = " ";
        }
        return result + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Adjustments that = (Adjustments) o;

        if (startX != that.startX) return false;
        if (!dx.equals(that.dx)) return false;
        if (!dy.equals(that.dy)) return false;
        if (!piece.equals(that.piece)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = piece.hashCode();
        result = 31 * result + startX;
        result = 31 * result + dx.hashCode();
        result = 31 * result + dy.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Adjustments{" +
                "piece=" + piece +
                ", startX=" + startX +
                ", dx=" + dx +
                ", dy=" + dy +
                '}';
    }
}

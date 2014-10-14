package com.github.tetrisanalyzer.settings.adjustment;

public class AdjustmentDxDy {
    public final int dx;
    public final int dy;

    public AdjustmentDxDy(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public int hashCode() {
        int result = dx;
        result = 31 * result + dy;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdjustmentDxDy that = (AdjustmentDxDy) o;

        if (dx != that.dx) return false;
        if (dy != that.dy) return false;

        return true;
    }

    @Override
    public String toString() {
        return "AdjustmentDxDy{" +
                "dx=" + dx +
                ", dy=" + dy +
                '}';
    }
}

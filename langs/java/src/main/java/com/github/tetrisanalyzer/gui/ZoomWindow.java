package com.github.tetrisanalyzer.gui;

public class ZoomWindow {
    public final double x1;
    public final double y1;
    public final double x2;
    public final double y2;

    public ZoomWindow() {
        this(0,0,1,1);
    }

    public ZoomWindow(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public double width() {
        return x2 - x1;
    }

    public double height() {
        return y2 - y1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ZoomWindow that = (ZoomWindow) o;

        if (Double.compare(that.x1, x1) != 0) return false;
        if (Double.compare(that.x2, x2) != 0) return false;
        if (Double.compare(that.y1, y1) != 0) return false;
        if (Double.compare(that.y2, y2) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x1);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(x2);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y2);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ZoomWindow{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                '}';
    }
}

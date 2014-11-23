package com.github.tetrisanalyzer.gui;

public class ZoomCalculator {

    public static ZoomWindow zoom(
            int width, int height,
            int x1, int y1, int x2, int y2,
            double wx1, double wy1, double wx2, double wy2) {

        double dx = (wx2 - wx1) / width;
        double dy = (wy2 - wy1) / height;

        double rx1 = wx1 + x1 * dx;
        double ry1 = wy1 + y1 * dy;
        double rx2 = wx1 + x2 * dx;
        double ry2 = wy1 + y2 * dy;

        if (rx1 < 0) rx1 = 0;
        if (ry1 < 0) ry1 = 0;
        if (rx2 < 0) rx2 = 0;
        if (ry2 < 0) ry2 = 0;
        if (rx1 > 1) rx1 = 1;
        if (ry1 > 1) ry1 = 1;
        if (rx2 > 1) rx2 = 1;
        if (ry2 > 1) ry2 = 1;

        return new ZoomWindow(rx1, ry1, rx2, ry2);
    }

    public static class ZoomWindow {
        public final double wx1;
        public final double wy1;
        public final double wx2;
        public final double wy2;

        public ZoomWindow(double wx1, double wy1, double wx2, double wy2) {
            this.wx1 = wx1;
            this.wy1 = wy1;
            this.wx2 = wx2;
            this.wy2 = wy2;
        }
    }
}


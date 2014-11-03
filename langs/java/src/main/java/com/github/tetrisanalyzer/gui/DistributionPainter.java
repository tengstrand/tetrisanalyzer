package com.github.tetrisanalyzer.gui;

import java.awt.*;

public class DistributionPainter {

    public static void paint(Graphics g, int x0, int y0, int width, int height, long[] cells) {
        int x1 = x0;
        int step = 2 - (width & 1);

        long maxValue = maxValue(step, cells);

        if (maxValue == 0) {
            return;
        }

        double dx = (double)width / cells.length * 2.5;

        int y1 = y0 + y(maxValue, cells[0], height);
        int x2;
        int y2;

        for (int x = 1; x<cells.length / 2.5; x++) {
            if (cells[x] > 0) {
                y2 = y0 + y(maxValue, cells[x], height);
                x2 = x0 + (int)(x * dx);
                g.setColor(Color.black);
                g.drawLine(x1, y1, x2, y2);
                x1 = x2;
                y1 = y2;
            }
        }
    }

    public static int y(double maxValue, long amount, int height) {
        return height - (int)((amount / maxValue) * height);
    }

    public static long maxValue(int step, long[] cells) {
        long max = 0;
        for (int i = 0; i<cells.length; i+=step) {
            if (cells[i] > max) {
                max = cells[i];
            }
        }
        return max;
    }
}

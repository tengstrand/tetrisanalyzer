package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.game.CellDistribution;

import java.awt.*;

public class DistributionPainter {

    public static void paint(Graphics g, int x0, int y0, int width, int height, CellDistribution distribution) {
        double maxValue = distribution.maxValue();
        int x1 = x0;

        double dx = (double)width / distribution.cells.length;

        if (maxValue == 0) {
            return;
        }

        int y1 = y0 + distribution.y(maxValue, distribution.cells[0], height);
        int x2;
        int y2;

        for (int x = 1; x<distribution.cells.length; x++) {
            long amount = distribution.cells[x] * x;

            if (amount > 0) {
                y2 = y0 + distribution.y(maxValue, amount, height);
                x2 = x0 + (int)(x * dx);
                g.setColor(Color.blue);
                g.drawLine(x1, y1, x2, y2);
                x1 = x2;
                y1 = y2;
            }
        }
    }
}

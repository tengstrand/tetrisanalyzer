package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.game.XY;

import java.awt.*;
import java.util.List;

public class Diagram {

    public static void draw(List<XY> coordinates, Graphics g) {
        if (coordinates.size() < 1) {
            return;
        }
        XY xy1 = coordinates.get(0);

        for (int i=1; i<coordinates.size(); i++) {
            XY xy2 = coordinates.get(i);
            g.drawLine(xy1.x, xy1.y, xy2.x, xy2.y);
            xy1 = xy2;
        }
    }
}

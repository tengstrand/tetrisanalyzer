package com.github.tetrisanalyzer.gui;

import java.awt.*;
import java.util.List;

public class Diagram {

    public static void draw(List<Vertex> coordinates, Graphics g) {
        if (coordinates.size() < 1) {
            return;
        }
        Vertex vertex1 = coordinates.get(0);

        for (int i=1; i<coordinates.size(); i++) {
            Vertex vertex2 = coordinates.get(i);
            g.drawLine(vertex1.x, vertex1.y, vertex2.x, vertex2.y);
            vertex1 = vertex2;
        }
    }
}

package com.github.tetrisanalyzer.gui;

import java.awt.*;

public class GraphBoardPainter {
    public int boardWidth;
    public int boardHeight;

    public GraphBoardPainter(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    public void paint(Graphics g, int x1, int y1, int cellWidth) {
        g.setColor(Color.gray);

        int x2 = x1 + boardWidth * cellWidth;
        int y2 = y1 + boardHeight * cellWidth;
        g.drawLine(x1, y1, x2, y1);
        g.drawLine(x2, y1, x2, y2);
        g.drawLine(x1, y2, x2, y2);
        g.drawLine(x1, y1, x1, y2);
    }
}

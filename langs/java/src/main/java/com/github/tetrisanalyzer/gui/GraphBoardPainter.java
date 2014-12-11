package com.github.tetrisanalyzer.gui;

import java.awt.*;

public class GraphBoardPainter {
    public int boardWidth;
    public int boardHeight;

    public GraphBoardPainter(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    public void paint(Graphics g, int x1, int y1, int width, int height, double row) {
        int cellWidth = width / boardWidth;
        if (height / boardHeight < cellWidth) {
            cellWidth = height / boardHeight;
        }
        if (cellWidth > 18)
            cellWidth = 18;

        int w = boardWidth * cellWidth;
        int h = boardHeight * cellWidth;
        int x2 = x1 + w;
        int y2 = y1 + h;

        int adjusty = height - cellWidth * boardHeight;
        y1 += adjusty;
        y2 += adjusty;

        // Write background
        g.setColor(new Color(235,235,235));
        g.fillRect(x1, y1, w, h);

        // Write frame
        g.setColor(Color.gray);
        g.drawLine(x1, y1, x2, y1);
        g.drawLine(x2, y1, x2, y2);
        g.drawLine(x1, y2, x2, y2);
        g.drawLine(x1, y1, x1, y2);

        g.setColor(Color.lightGray);

        int y = (int)row;
        if (row >= 1) {
            h = y * cellWidth - 1;
            g.fillRect(x1+1, y2-h, boardWidth*cellWidth-1, h);
        }
        w = (int) ((row - y) * boardWidth * cellWidth) - 1;
        y = y2 - (y+1) * cellWidth + 1;
        g.fillRect(x1+1, y, w, cellWidth-1);

        g.setColor(Color.white);
        for (int x=x1+cellWidth; x<x2; x += cellWidth) {
            g.drawLine(x, y1+1, x, y2-1);
        }
        for (y=y1 + cellWidth; y<y2; y += cellWidth) {
            g.drawLine(x1+1, y, x2-1, y);
        }
    }
}

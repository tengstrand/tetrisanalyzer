package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.settings.RaceGameSettings;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class Graph implements MouseListener, MouseMotionListener {
    private int x;
    private int y;
    private int width;
    private int height;

    public int startIdx;
    public int endIdx;
    private int maxIdx;

    public int startSelectionIdx;
    public int endSelectionIdx;

    private int mouseSelectX1;
    private int mouseSelectX2;

    private List<RaceGameSettings> games;

    public Graph(int x, int y, int width, int height, int startIdx, int endIdx, List<RaceGameSettings> games) {
        this(x, y, width, height, startIdx, endIdx, -1, -1, games);
    }

    public Graph(int x, int y, int width, int height, int startIdx, int endIdx,
                 int startSelectionIdx, int endSelectionIdx, List<RaceGameSettings> games) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startIdx = startIdx;
        this.endIdx = endIdx;
        this.startSelectionIdx = startSelectionIdx;
        this.endSelectionIdx = endSelectionIdx;
        this.games = games;
        maxIdx = games.get(0).distribution.cells.length - 1;
    }

    public void adjustStartIndex(int dx) {
        int index = startIdx + dx;
        if (startIdx > endIdx) {
            startIdx = endIdx;
        } else {
            startIdx = index >= 0 ? index : 0;
        }
    }

    public void adjustEndIndex(int dx) {
        int index = endIdx + dx;
        if (index < startIdx) {
            endIdx = startIdx;
        } else {
            endIdx = index > maxIdx ? maxIdx : index;
        }
    }

    public void draw(Graphics g) {
        fillMouseSelection(g);

        if (startSelectionIdx >= 0) {
            g.setColor(Color.gray);
        }
        for (RaceGameSettings game : games) {
            Lines lines = game.distribution.lines(startIdx, endIdx, width, height);
            if (startSelectionIdx < 0) {
                g.setColor(game.color);
                lines.drawLines(x, y, g);
            } else {
                lines.drawLines(x, y, g);
                lines.drawSelection(x, y, startSelectionIdx, endSelectionIdx, g);
            }
        }
    }

    private void fillMouseSelection(Graphics g) {
        int x1;
        int x2;
        if (mouseSelectX1 < mouseSelectX2) {
            x1 = mouseSelectX1;
            x2 = mouseSelectX2;
        } else {
            x1 = mouseSelectX2;
            x2 = mouseSelectX1;
        }
        g.setColor(Color.lightGray);
        g.fillRect(x1, y, x2 - x1 + 1, height + 1);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() >= x && e.getX() <= x + width) {
            mouseSelectX1 = mouseSelectX2 = e.getX();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseSelectX2 = e.getX();

        // do stuff

        mouseSelectX1 = -1;
        mouseSelectX2 = -1;
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}

    @Override public void mouseDragged(MouseEvent e) {
        if (mouseSelectX1 > 0) {
            mouseSelectX2 = calculateX(e);
        }
    }

    private int calculateX(MouseEvent e) {
        int sx = e.getX();
        if (sx > x + width) {
            sx = x + width;
        }
        if (sx < x) {
            sx = x;
        }
        return sx;
    }
}

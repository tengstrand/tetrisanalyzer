package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.settings.RaceGameSettings;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class Graph implements MouseListener, MouseMotionListener {
    private int x1;
    private int x2;
    private int y;
    private int width;
    private int height;

    public double start;
    public double end;
    private int maxIdx;

    public double startSelection;
    public double endSelection;

    private static int START_DX = 400;

    private boolean mouseButtonPressed;
    private boolean expandLeft;
    private boolean expandRight;
    private long expandTime;
    private int dx = START_DX;
    private int lastExpandX;
    private int mouseSelectX1;
    private int mouseSelectX2;

    private List<RaceGameSettings> games;

    public Graph(int x1, int y, int width, int height, double start, double end, List<RaceGameSettings> games) {
        this(x1, y, width, height, start, end, -1, -1, games);
    }

    public Graph(int x, int y, int width, int height, double start, double end,
                 double startSelection, double endSelection, List<RaceGameSettings> games) {
        this.x1 = x;
        this.x2 = x + width;
        this.y = y;
        this.width = width;
        this.height = height;
        this.start = start;
        this.end = end;
        this.startSelection = startSelection;
        this.endSelection = endSelection;
        this.games = games;
        maxIdx = games.get(0).distribution.cells.length - 1;
    }

    public void setSelection(Graph graph) {
        // TODO: fix!
    }

    public void draw(Graphics g) {
        fillMouseSelection(g);

        for (RaceGameSettings game : games) {
            Lines lines = game.distribution.lines(0, 1, 0, 1, width, height);
            g.setColor(game.color);
            lines.drawLines(x1, y, g);
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
        if (e.getX() >= x1 && e.getX() <= x2) {
            mouseSelectX1 = mouseSelectX2 = e.getX();
            mouseButtonPressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseSelectX2 = e.getX();
        mouseButtonPressed = false;
        expandRight = false;
        dx = START_DX;

        //TODO: do stuff!

        mouseSelectX1 = -1;
        mouseSelectX2 = -1;
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}

    @Override public void mouseDragged(MouseEvent e) {
        if (mouseSelectX1 > 0) {
            expandLeft = e.getX() < x1 && e.getX() < lastExpandX && mouseButtonPressed;
            expandRight = e.getX() > x2 && e.getX() > lastExpandX && mouseButtonPressed;
            mouseSelectX2 = calculateX(e);
            lastExpandX = e.getX();
        }
    }

    private int calculateX(MouseEvent e) {
        int sx = e.getX();
        if (sx > x2) {
            sx = x2;
        }
        if (sx < x1) {
            sx = x1;
        }
        return sx;
    }
}

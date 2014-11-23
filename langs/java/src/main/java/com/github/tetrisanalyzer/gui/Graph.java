package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.settings.RaceGameSettings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class Graph implements MouseListener, MouseMotionListener, KeyListener {
    private int x1;
    private int x2;
    private int y;
    private int width;
    private int height;

    public double wx1;
    public double wx2;
    public double wy1;
    public double wy2;

    private int sx1 = -1;
    private int sy1 = -1;
    private int sx2 = -1;
    private int sy2 = -1;

    private List<RaceGameSettings> games;

    public Graph(int x, int y, int width, int height, List<RaceGameSettings> games) {
        this(x, y, width, height, 0, 1, 0, 1, games);
    }

    public Graph(int x, int y, int width, int height, double wx1, double wx2, double wy1, double wy2, List<RaceGameSettings> games) {
        this.x1 = x;
        this.x2 = x + width;
        this.y = y;
        this.width = width;
        this.height = height;
        this.wx1 = wx1;
        this.wx2 = wx2;
        this.wy1 = wy1;
        this.wy2 = wy2;
        this.games = games;
    }

    public void setSelection(Graph graph) {
        // TODO: fix!
    }

    public void draw(Graphics g) {
        fillMouseSelection(g);

        for (RaceGameSettings game : games) {
            Lines lines = game.distribution.lines(wx1, wy1, wx2, wy2, width, height);
            g.setColor(game.color);
            lines.drawLines(x1, y, g);
        }
    }

    private void fillMouseSelection(Graphics g) {
        if (sx1 >= 0) {
            int mx1,mx2,my1,my2;
            if (sx1 <= sx2) {
                mx1 = sx1;
                mx2 = sx2;
            } else {
                mx1 = sx2;
                mx2 = sx1;
            }
            if (sy1 <= sy2) {
                my1 = sy1;
                my2 = sy2;
            } else {
                my1 = sy2;
                my2 = sy1;
            }
            g.setColor(Color.lightGray);
            g.fillRect(mx1, my1, mx2 - mx1 + 1, my2 - my1 + 1);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() >= x1 && e.getX() <= x2) {
            sx1 = sx2 = e.getX();
            sy1 = sy2 = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        sx1 = -1;
        sx2 = -1;
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}

    @Override public void mouseDragged(MouseEvent e) {
        if (sx1 >= 0) {
            sx2 = e.getX();
            sy2 = e.getY();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("KeyPressed: " + e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.settings.RaceGameSettings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.Stack;

import static com.github.tetrisanalyzer.gui.ZoomCalculator.ZoomWindow;

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

    private static Color grey = new Color(230, 230, 230);

    Stack<ZoomWindow> windows = new Stack<>();

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

        windows.add(new ZoomWindow(wx1, wy1, wx2, wy2));
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
            SelectedWindow w = selectedWindow();
            g.setColor(grey);
            g.fillRect(w.x1, w.y1, w.x2 - w.x1 + 1, w.y2 - w.y1 + 1);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() >= x1 && e.getX() <= x2) {
            if (e.getButton() == 1) {
                sx1 = sx2 = e.getX();
                sy1 = sy2 = e.getY();
            } else if (e.getButton() == 3 && !windows.isEmpty()) {
                ZoomWindow window = windows.pop();
                wx1 = window.x1;
                wy1 = window.y1;
                wx2 = window.x2;
                wy2 = window.y2;
            }
        }
    }

    private SelectedWindow selectedWindow() {
        int x1,x2,y1,y2;
        if (sx1 <= sx2) {
            x1 = sx1;
            x2 = sx2;
        } else {
            x1 = sx2;
            x2 = sx1;
        }
        if (sy1 <= sy2) {
            y1 = sy1;
            y2 = sy2;
        } else {
            y1 = sy2;
            y2 = sy1;
        }
        return new SelectedWindow(x1, y1, x2, y2);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() != 1 || sx1 < 0 || sx1 == sx2) {
            return;
        }
        SelectedWindow w = selectedWindow();
        sx1 = -1;

        windows.add(new ZoomWindow(wx1, wy1, wx2, wy2));

        ZoomWindow window = ZoomCalculator.zoom(width, height, w.x1 - x1, w.y1 - y, w.x2 - x1, w.y2 - y, wx1, wy1, wx2, wy2);
        wx1 = window.x1;
        wy1 = window.y1;
        wx2 = window.x2;
        wy2 = window.y2;
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

    @Override public void keyTyped(KeyEvent e) {}

    @Override public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 86) {
        }
    }
}

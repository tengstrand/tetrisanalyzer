package com.github.tetrisanalyzer.gui.graph;

import com.github.tetrisanalyzer.gui.*;
import com.github.tetrisanalyzer.settings.RaceGameSettings;
import com.github.tetrisanalyzer.text.RaceInfo;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class Graph implements MouseListener, MouseMotionListener, KeyListener {
    protected int x1;
    protected int x2;
    protected int y1;
    protected int width;
    protected int height;
    private int sx1 = -1;
    private int sy1 = -1;
    private int sx2 = -1;
    private int sy2 = -1;

    private int dragX = -1;
    private int dragY = -1;

    private int charWidth;
    private final RaceInfo raceInfo;
    protected final List<RaceGameSettings> games;

    private static Color grey = new Color(230, 230, 230);
    private static Color lightGrey = new Color(240, 240, 240);

    private static double zoomSpeed = 0.9;
    private static double fastZoomSpeed = 0.5;
    private Zoomer zoomer;
    private Stack<ZoomWindow> windows = new Stack<>();

    private ZoomWindow overview = new ZoomWindow();

    private final Shortcuts shortcuts;

    public Graph(int x1, int y1, List<RaceGameSettings> games, Shortcuts shortcuts, RaceInfo raceInfo) {
        this.x1 = x1;
        this.y1 = y1;
        this.games = games;
        this.shortcuts = shortcuts;
        this.raceInfo = raceInfo;
        windows.add(new ZoomWindow());
    }

    public abstract void draw(boolean background, int x1, int y1, int width, int height, Graphics g);

    public void setParameters(int x1, int y1, int width, int height, Graphics g) {
        this.x1 = x1;
        this.y1 = y1;
        this.width = width;
        this.height = height;
        this.x2 = x1 + width;

        charWidth = g.getFontMetrics().charWidth(' ');
    }

    protected void paintGraph(boolean background, double[] values, Graphics g) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (double value : values) {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }

        // 2. Cut off the "floor".
        max = max - min;

        for (int i=0; i<values.length; i++) {
            values[i] = values[i] - min;
        }

        // 3. Create vertices
        List<Vertex> vertices = new ArrayList<>();

        double dx = 1.0 / (values.length - 1);
        for (int i=0; i<values.length; i++) {
            vertices.add(new Vertex(i * dx, values[i]));
        }

        // 4. Clip lines and scale.
        ZoomWindow w = currentWindow();
        Lines lines = new Vertices(vertices)
                .normalizeY(max)
                .clipHorizontal(w.x1, w.x2)
                .clipVertically(w.y1, w.y2)
                .resize(w.x1, w.y1, w.x2, w.y2, width, height);

        // 5. Paint
        if (background) {
            g.setColor(lightGrey);
            lines.drawLines(x1, y1, g);
        } else {
            Color[] colors = new Color[values.length];
            int i = 0;
            for (RaceGameSettings game : this.games) {
                colors[i++] = game.color;
            }
            lines.drawColorredLines(x1, y1, colors, g);
        }
    }

    public ZoomWindow currentWindow() {
        if (zoomer != null && zoomer.isZooming()) {
            return zoomer.zoom();
        }
        return windows.peek();
    }

    public String export() {
        return shortcuts.export();
    }

    protected void fillMouseSelection(Graphics g) {
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
                dragX = -1;
            } else if (e.getButton() == 3) {
                sx1 = -1;
                dragX = e.getX();
                dragY = e.getY();
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
        dragX = dragY = -1;
        if (e.getButton() != 1 || sx1 < 0 || sx1 == sx2) {
            return;
        }
        SelectedWindow sw = selectedWindow();
        sx1 = -1;

        ZoomWindow w = windows.peek();
        ZoomWindow window = ZoomCalculator.zoom(width, height, sw.x1 - x1, sw.y1 - y1, sw.x2 - x1, sw.y2 - y1, w.x1, w.y1, w.x2, w.y2);

        if (window.width() > 0 && window.height() > 0) {
            zoomer = Zoomer.zoomIn(w, window, zoomSpeed);
            windows.push(window);
        }
    }

    @Override public void mouseClicked(MouseEvent e) {
        if (e.getY() < raceInfo.height()) {
            int column = raceInfo.translatePixeltoColumn(e.getX(), charWidth);
            if (column >= 0) {
                raceInfo.height();
                games.get(column).game.togglePermanentlyPaused();
            }
        }

        if (e.getX() >= x1 && e.getX() <= x2 && e.getButton() == 3 && windows.size() > 1) {
            ZoomWindow from = windows.pop();
            zoomer = Zoomer.zoomOut(from, windows.peek(), zoomSpeed);
        }
    }

    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}

    @Override public void mouseDragged(MouseEvent e) {
        if (e.getModifiers() == 16 && sx1 >= 0) {
            sx2 = e.getX();
            sy2 = e.getY();
        } else if (e.getModifiers() == 4 || e.getModifiers() == 20) {
            double dx = e.getX() - dragX;
            double dy = e.getY() - dragY;

            ZoomWindow w = windows.peek();
            double wx = w.x2 - w.x1;
            double wy = w.y2 - w.y1;

            double wdx = wx * (dx / width);
            double wdy = wy * (dy / height);

            if (w.x1 - wdx < 0) { wdx = w.x1; }
            if (w.y1 - wdy < 0) { wdy = w.y1; }
            if (w.x2 - wdx > 1) { wdx = w.x2 - 1; }
            if (w.y2 - wdy > 1) { wdy = w.y2 - 1; }
            windows.pop();
            windows.push(new ZoomWindow(w.x1 - wdx, w.y1 - wdy, w.x2 - wdx, w.y2 - wdy));
            dragX = e.getX();
            dragY = e.getY();
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    @Override public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int modifiers = e.getModifiers();
        if (key >= 48 && key <= 57) { // 0..9
            int index = key - 48;

            if ((modifiers == 2 || modifiers == 4) && index > 0) {
                shortcuts.set(index, windows);
            } else if (modifiers == 1) {
                zoomTo(index, fastZoomSpeed);
            } else {
                zoomTo(index, zoomSpeed);
            }
        }
    }

    private void zoomTo(int index, double zoomSpeed) {
        ZoomWindow from = windows.peek();
        Stack<ZoomWindow> toWindows = shortcuts.get(index);
        if (toWindows.peek() == from && from != overview) {
            windows = shortcuts.get(0);
            zoomer = Zoomer.zoomOut(from, overview, zoomSpeed);
        } else {
            windows = toWindows;
            zoomer = Zoomer.zoomOutAndIn(from, windows.peek(), zoomSpeed);
        }
    }

    public boolean isZoomed() {
        return windows.size() > 1 || (zoomer != null && zoomer.isZooming());
    }
}

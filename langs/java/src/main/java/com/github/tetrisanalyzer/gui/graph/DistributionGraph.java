package com.github.tetrisanalyzer.gui.graph;

import com.github.tetrisanalyzer.gui.*;
import com.github.tetrisanalyzer.settings.RaceGameSettings;
import com.github.tetrisanalyzer.text.RaceInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DistributionGraph extends Graph {

    public double areaPercentage;

    public DistributionGraph(int x, int y, List<RaceGameSettings> games, Shortcuts shortcuts, RaceInfo raceInfo) {
        super(x, y, games, shortcuts, raceInfo);
    }

    public void draw(Graphics g, int x1, int y1, int width, int height) {
        setParameters(x1, y1, width, height, g);
        fillMouseSelection(g);

        Double maxYRatio = Double.MIN_VALUE;
        List<Vertices> gameVertices = new ArrayList<>(games.size());

        // 1. Calculate the graph and the relative max y (maxYRatio).
        for (RaceGameSettings game : games) {
            Vertices vertices = game.distribution.toVertices();
            gameVertices.add(vertices);

            if (vertices.maxYRatio() > maxYRatio) {
                maxYRatio = vertices.maxYRatio();
            }
        }

        // 2. Clip lines, scale and paint.
        Iterator<RaceGameSettings> gameIterator = games.iterator();
        for (Vertices vertices : gameVertices) {
            ZoomWindow w = currentWindow();
            Lines lines = vertices
                    .normalizeTotalY(maxYRatio)
                    .clipHorizontal(w.x1, w.x2)
                    .clipVertically(w.y1, w.y2)
                    .resize(w.x1, w.y1, w.x2, w.y2, width, height);
            g.setColor(gameIterator.next().color);
            lines.drawLines(x1, y1, g);
        }

        printAreaPercentBar(g, x1, y1, width, height);
    }

    protected void printAreaPercentBar(Graphics g, int x1, int y1, int width, int height) {
        g.setColor(Color.lightGray);
        double x = (100 - areaPercentage) / 100.0;
        ZoomWindow w = currentWindow();

        List<Vertex> vertexes = new ArrayList<>();
        vertexes.add(new Vertex(x, 0));
        vertexes.add(new Vertex(x, 1));

        new Vertices(vertexes)
                .removeVerticalLineIfOutside(w.x1, w.x2)
                .clipVertically(w.y1, w.y2)
                .resize(w.x1, w.y1, w.x2, w.y2, width, height)
                .drawLines(x1, y1, g);
    }

    public void drawSelection(ZoomWindow w, Graphics g) {
        int wx1 = (int)(x1 + width * w.x1);
        int wy1 = (int)(y1 + height * w.y1);

        int width = (int)(this.width * w.width());
        int height = (int)(this.height * w.height());
        if (width < 1) { wx1 -= 1; width = 1; }
        if (height < 1) { wy1 -= 1; height = 1; }

        g.setColor(new Color(255, 100, 0));
        g.drawRect(wx1, wy1, width+1, height+1);
    }
}

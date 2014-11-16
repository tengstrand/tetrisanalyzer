package com.github.tetrisanalyzer.gui;

import com.github.tetrisanalyzer.settings.RaceGameSettings;

import java.awt.*;
import java.util.List;

public class Graph {
    private int x;
    private int y;
    private int width;
    private int height;

    public int startIdx;
    public int endIdx;
    private int maxIdx;

    public int startSelectionIdx;
    public int endSelectionIdx;

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
}

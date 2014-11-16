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
    }

    public void draw(Graphics g) {
        for (RaceGameSettings game : games) {
            Lines lines = game.distribution.lines(startIdx, endIdx, width, height);
            if (startSelectionIdx < 0) {
                g.setColor(game.color);
                lines.drawLines(x, y, g);
            } else {
                lines.drawLines(x, y, g);
                lines.drawSelection(x, y, startSelectionIdx, endSelectionIdx, height, g);

            }
        }
    }
}

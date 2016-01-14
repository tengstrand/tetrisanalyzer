package com.github.tetrisanalyzer.gui.graph;

import com.github.tetrisanalyzer.gui.Shortcuts;
import com.github.tetrisanalyzer.settings.RaceGameSettings;
import com.github.tetrisanalyzer.text.RaceInfo;

import java.awt.*;

public class DistributionAreasGraph extends Graph {

    public DistributionAreasGraph(int x, int y, java.util.List<RaceGameSettings> games, Shortcuts shortcuts, RaceInfo raceInfo) {
        super(x, y, games, shortcuts, raceInfo);
    }

    @Override
    public void draw(Graphics g, int x1, int y1, int width, int height) {
        setParameters(x1, y1, width, height, g);
        fillMouseSelection(g);

        double[] areas = new double[games.size()];

        int i = 0;
        for (RaceGameSettings game : games) {
            areas[i++] = 100 - game.gameState.area();
        }
        paintGraph(areas, g);
    }
}

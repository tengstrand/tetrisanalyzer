package com.github.tetrisanalyzer.gui.graph;

import com.github.tetrisanalyzer.gui.Shortcuts;
import com.github.tetrisanalyzer.settings.RaceGameSettings;
import com.github.tetrisanalyzer.text.RaceInfo;

import java.awt.*;

public class AreasGraph extends Graph {

    public AreasGraph(int x, int y, java.util.List<RaceGameSettings> games, Shortcuts shortcuts, RaceInfo raceInfo) {
        super(x, y, games, shortcuts, raceInfo);
    }

    @Override
    public void draw(boolean distributionInTheBackground, int x1, int y1, int width, int height, Graphics g) {
        setParameters(x1, y1, width, height, g);
        fillMouseSelection(g);

        if (distributionInTheBackground) {
            paintDistributionAreaGraph(distributionInTheBackground, g);
            paintRowsPerGameGraph(!distributionInTheBackground, g);
        } else {
            paintRowsPerGameGraph(!distributionInTheBackground, g);
            paintDistributionAreaGraph(distributionInTheBackground, g);
        }
    }

    private void paintDistributionAreaGraph(boolean background, Graphics g) {
        double[] areas = new double[games.size()];

        int i = 0;
        for (RaceGameSettings game : games) {
            areas[i++] = 100 - game.gameState.area();
        }
        paintGraph(background, areas, g);
    }

    private void paintRowsPerGameGraph(boolean background, Graphics g) {
        double[] rowspergame = new double[games.size()];

        int i = 0;
        for (RaceGameSettings game : games) {
            rowspergame[i++] = game.gameState.rowsPerGame();
        }
        paintGraph(background, rowspergame, g);
    }
}

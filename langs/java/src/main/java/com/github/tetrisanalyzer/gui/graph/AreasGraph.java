package com.github.tetrisanalyzer.gui.graph;

import com.github.tetrisanalyzer.gui.Shortcuts;
import com.github.tetrisanalyzer.gui.ViewMode;
import com.github.tetrisanalyzer.settings.RaceGameSettings;
import com.github.tetrisanalyzer.settings.RaceGamesSettings;

import java.awt.*;

public class AreasGraph extends Graph {

    public AreasGraph(int x, int y, RaceGamesSettings games, Shortcuts shortcuts) {
        super(x, y, games, shortcuts);
    }

    @Override
    public void draw(ViewMode viewMode, int x1, int y1, int width, int height, Graphics g) {
        setParameters(x1, y1, width, height);
        fillMouseSelection(g);

        switch (viewMode) {
            case ROWS_PER_GAME:
                paintDistributionAreaGraph(true, g);
                paintRowsPerGameGraph(false, g);
                break;
            case DISTRIBUTION_AREA:
                paintRowsPerGameGraph(true, g);
                paintDistributionAreaGraph(false, g);
                break;
            case EQUITY_DIFF:
                paintEquityAbsDiffGraph(g);
                break;
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

    private void paintEquityAbsDiffGraph(Graphics g) {
        double[] equitydiff = new double[games.size()];

        int i = 0;
        for (RaceGameSettings game : games) {
            equitydiff[i++] = 10000000 - game.gameState.equityAbsDiffPerPiece();
        }
        paintGraph(false, equitydiff, g);
    }
}

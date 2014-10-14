package com.github.tetrisanalyzer.settings;

public class StartPieceCalculator {

    public static int startX(int boardWidth, int startPieceXstandardBoard) {
        double dx = startPieceXstandardBoard;
        double dy = 10 - dx + .0001;
        return (int)((boardWidth - dx + .0001) * (dx/dy));
    }
}

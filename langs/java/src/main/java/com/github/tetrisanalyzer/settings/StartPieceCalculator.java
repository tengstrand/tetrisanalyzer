package com.github.tetrisanalyzer.settings;

public class StartPieceCalculator {

    public static int startX(int boardWidth, int startPieceXstandardBoard) {
        double dx = startPieceXstandardBoard;
        double dy = 10 - dx;
        return (int)((boardWidth - dx) * (dx/dy));
    }
}

package com.github.tetrisanalyzer.settings;

public class StartPieceCalculator {

    public static int startX(int boardWidth, int startPosXBoardWidthTen) {
        double dx = startPosXBoardWidthTen;
        double dy = 10 - dx + .0001;
        return (int)((boardWidth - dx + .0001) * (dx/dy));
    }
}

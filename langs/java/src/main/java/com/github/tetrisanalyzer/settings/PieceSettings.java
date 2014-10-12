package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.rotation.RotationDirection;

public interface PieceSettings {
    RotationDirection rotationDirection();
    int pieceStartX();
    int[] startPieceDx();
    int[][] pieceDx();
    int[][] pieceDy();
}

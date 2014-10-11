package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;

/**
 * The default settings used by the Tetris engine.
 */
public class DefaultGameSettings implements GameSettings {

    private final int boardWidth;
    private final int boardHeight;
    private final int pieceStartX;

    public DefaultGameSettings() {
        this(10, 20, 4);
    }

    public DefaultGameSettings(int boardWidth, int boardHeight, int pieceStartX) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.pieceStartX = pieceStartX;
    }

    public boolean isSlidingEnabled() {
        return false;
    }

    public RotationDirection rotationDirection() {
        return new AnticlockwiseRotation();
    }

    @Override
    public int pieceStartX() {
        return pieceStartX;
    }

    @Override
    public int boardWidth() {
        return boardWidth;
    }

    @Override
    public int boardHeight() {
        return boardHeight;
    }
}

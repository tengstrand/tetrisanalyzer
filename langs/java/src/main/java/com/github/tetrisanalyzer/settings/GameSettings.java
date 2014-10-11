package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.rotation.RotationDirection;

/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
public interface GameSettings {
    boolean isSlidingEnabled();
    RotationDirection rotationDirection();
    int pieceStartX();
    int boardWidth();
    int boardHeight();
}

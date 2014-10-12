package com.github.tetrisanalyzer.settings;

/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
public interface GameSettings extends PieceSettings {
    String name();
    String info();
    boolean isSlidingEnabled();
    int boardWidth();
    int boardHeight();
}

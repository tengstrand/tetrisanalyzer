package com.github.tetrisanalyzer.settings;

/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
public interface GameSettings extends PieceSettings {
    String id();
    String url();
    String description();
    String export();
    boolean isSlidingEnabled();
    int boardWidth();
    int boardHeight();
}

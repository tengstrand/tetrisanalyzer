package com.github.tetrisanalyzer.settings;

/**
 * Settings introduced by Colin Fahey:
 *   http://colinfahey.com/tetris/tetris.html
 */
public class StandardTetrisGameSettings extends DefaultGameSettings {

    public StandardTetrisGameSettings() {
        this(10, 20, 3);
    }

    public StandardTetrisGameSettings(int boardWidth, int boardHeight, int pieceStartX) {
        super(boardWidth, boardHeight, pieceStartX);
    }
}

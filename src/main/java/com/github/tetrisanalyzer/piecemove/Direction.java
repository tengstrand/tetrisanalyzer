package com.github.tetrisanalyzer.piecemove;

/**
 * Type of piece move: Left, Right, Down and Rotate
 */
public enum Direction {
    DOWN(0),
    ROTATE(1),
    LEFT(2),
    RIGHT(3);

    private int index;

    Direction(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}

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

    public static int NUMBER_OF_DIRECTIONS = 4;

    Direction(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}

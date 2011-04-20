package com.github.tetrisanalyzer.move;

import com.github.tetrisanalyzer.move.rotation.RotationDirection;

/**
 * Holds a position (x,y) and rotation ("angle") for a piece on the board.
 */
public class Move {
    private int rotation;
    private int x;
    private int y;

    public Move(int rotation, int x, int y) {
        this.rotation = rotation;
        this.x = x;
        this.y = y;
    }

    public Move up() { return new Move(rotation, x, y-1); }
    public Move down() { return new Move(rotation, x, y+1); }
    public Move left() { return new Move(rotation, x-1, y); }
    public Move right() { return new Move(rotation, x+1, y); }
    public Move rotate(RotationDirection rotationType, int rotationModulus) {
        return new Move((rotation + rotationType.getDirection()) & rotationModulus, x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (rotation != move.rotation) return false;
        if (x != move.x) return false;
        if (y != move.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rotation;
        result = 31 * result + x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "(" + rotation + "," + x + ", " + y + ")";
    }
}

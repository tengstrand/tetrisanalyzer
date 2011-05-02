package com.github.tetrisanalyzer.piece;

public class PieceShape {
    private Point[] shape;

    public PieceShape(Point... shape) {
        this.shape = shape;
    }

    public Point[] getPoints() {
        return shape;
    }
}

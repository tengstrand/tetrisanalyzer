package com.github.tetrisanalyzer.piece;

/**
 * Represents one of the pieces: I, Z, S, J, L, T or O.
 */
public abstract class Piece {
    private static int[] ROTATION_MODULUS = { 0, 1, 0, 3 };
    private static Piece[] PIECES = { new PieceEmpty(), new PieceI(), new PieceZ(), new PieceS(), new PieceJ(), new PieceL(), new PieceT(), new PieceO(), new PieceAny() };

    public static int NUMBER_OF_PIECE_TYPES = 7;

    public static Piece get(int pieceNumber) {
        return PIECES[pieceNumber];
    }

    public abstract byte number();
    public abstract char character();
    public int rotationsEndIndex() { return heights().length - 1; }
    public int rotationModulus() { return ROTATION_MODULUS[rotationsEndIndex()]; }
    public int width(int rotation) { return widths()[rotation]; }
    public int height(int rotation) { return heights()[rotation]; }
    public PieceShape getShape(int rotation) { return shapes()[rotation]; }
    protected abstract int[] widths();
    protected abstract int[] heights();
    protected abstract PieceShape[] shapes();

    @Override
    public int hashCode() {
        return number();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Piece)) {
            return false;
        }
        return ((Piece)obj).number() == number();
    }

    @Override
    public String toString() {
        return String.valueOf(character());
    }
}

package com.github.tetrisanalyzer.piece;

import com.github.tetrisanalyzer.settings.PieceSettings;

/**
 * Represents one of the pieces: O, I, S, Z, L, J, T
 */
public abstract class Piece {
    private static final int[] ROTATION_MODULUS = { 0, 1, 0, 3 };

    public static final int NUMBER_OF_PIECE_TYPES = 7;

    public static final int O = 1;
    public static final int I = 2;
    public static final int S = 3;
    public static final int Z = 4;
    public static final int L = 5;
    public static final int J = 6;
    public static final int T = 7;

    public static Piece[] pieces(PieceSettings settings) {
        Piece[] pieces = new Piece[] {
                new PieceEmpty(settings),
                new PieceO(settings),
                new PieceI(settings),
                new PieceS(settings),
                new PieceZ(settings),
                new PieceL(settings),
                new PieceJ(settings),
                new PieceT(settings),
                new PieceAny(settings)
        };
        return pieces;
    }

    public Piece(PieceSettings settings) {
        startDx = settings.pieceStartDx()[number()];
        dx = settings.pieceDx()[number()];
        dy = settings.pieceDy()[number()];
    }

    public final int startDx;
    public final int[] dx;
    public final int[] dy;

    public abstract byte number();
    public abstract char character();
    public int rotationsEndIndex() { return heights().length - 1; }
    public int rotationModulus() { return ROTATION_MODULUS[rotationsEndIndex()]; }
    public int width(int rotation) { return widths()[rotation]; }
    public int height(int rotation) { return heights()[rotation]; }
    public int dx(int rotation) { return dx[rotation]; };
    public int dy(int rotation) { return dy[rotation]; };
    public boolean isAdjusted(int rotation) { return dx(rotation) != 0 || dy(rotation) != 0; }
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

package com.github.tetrisanalyzer.piece;

import com.github.tetrisanalyzer.settings.PieceSettings;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import java.util.HashMap;
import java.util.Map;

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

    public static PieceEmpty createPieceEmpty(PieceSettings settings) { return new PieceEmpty(settings); }
    public static PieceO createPieceO(PieceSettings settings) { return new PieceO(settings); }
    public static PieceI createPieceI(PieceSettings settings) { return new PieceI(settings); }
    public static PieceS createPieceS(PieceSettings settings) { return new PieceS(settings); }
    public static PieceZ createPieceZ(PieceSettings settings) { return new PieceZ(settings); }
    public static PieceL createPieceL(PieceSettings settings) { return new PieceL(settings); }
    public static PieceJ createPieceJ(PieceSettings settings) { return new PieceJ(settings); }
    public static PieceT createPieceT(PieceSettings settings) { return new PieceT(settings); }
    public static PieceAny createPieceAny(PieceSettings settings) { return new PieceAny(settings); }

    public static Piece[] validPieces(PieceSettings settings) {
        Piece[] pieces = new Piece[] {
                createPieceEmpty(settings),
                createPieceO(settings),
                createPieceI(settings),
                createPieceS(settings),
                createPieceZ(settings),
                createPieceL(settings),
                createPieceJ(settings),
                createPieceT(settings),
                createPieceAny(settings)
        };
        return pieces;
    }

    private static final Map<Character,Integer> indexMap = new HashMap<>();

    static {
        indexMap.put('-', 0);
        indexMap.put('O', 1);
        indexMap.put('I', 2);
        indexMap.put('S', 3);
        indexMap.put('Z', 4);
        indexMap.put('L', 5);
        indexMap.put('J', 6);
        indexMap.put('T', 7);
        indexMap.put('x', 8);
    }

    public static int indexOf(char piece) {
        return indexMap.get(piece);
    }

    public Piece(PieceSettings settings) {
        adjustments = settings.pieceAdjustments()[number()];
    }

    public final Adjustments adjustments;

    public abstract byte number();
    public abstract char character();
    public int rotationsEndIndex() { return heights().length - 1; }
    public int rotationModulus() { return ROTATION_MODULUS[rotationsEndIndex()]; }
    public int width(int rotation) { return widths()[rotation]; }
    public int height(int rotation) { return heights()[rotation]; }
    public int startX() { return adjustments.startX; }
    public int dx(int rotation) { return adjustments.dx.get(rotation); };
    public int dy(int rotation) { return adjustments.dy.get(rotation); };
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

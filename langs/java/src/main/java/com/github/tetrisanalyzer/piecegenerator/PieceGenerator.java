package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.PieceSettings;

public abstract class PieceGenerator {

    public abstract PieceGenerator copy();
    public abstract String export();
    public abstract int nextPieceNumber();

    public Piece nextPiece(PieceSettings settings) {
        int pieceNumber = nextPieceNumber();

        if (pieceNumber < 1 || pieceNumber > 7) {
            throw new IllegalArgumentException("Piece number must be in the range 1..7, found: " + pieceNumber);
        }
        return settings.piece(pieceNumber);
    }

    @Override
    public String toString() {
        return export();
    }
}

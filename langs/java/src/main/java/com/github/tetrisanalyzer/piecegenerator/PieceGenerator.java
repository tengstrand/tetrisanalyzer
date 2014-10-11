package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.PieceSettings;

public abstract class PieceGenerator {
    private Piece[] pieces;

    public abstract int nextPieceNumber();

    protected PieceGenerator(PieceSettings settings) {
         pieces = Piece.pieces(settings);
    }

    public Piece nextPiece() {
        int pieceNumber = nextPieceNumber();

        if (pieceNumber < 1 || pieceNumber > 7) {
            throw new IllegalArgumentException("Piece number must be in the range 1..7, found: " + pieceNumber);
        }
        return pieces[pieceNumber];
    }
}

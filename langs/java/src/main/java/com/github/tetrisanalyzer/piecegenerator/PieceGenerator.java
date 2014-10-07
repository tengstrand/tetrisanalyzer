package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;

public abstract class PieceGenerator {
    public abstract int nextPieceNumber();

    public Piece nextPiece() {
        int pieceNumber = nextPieceNumber();

        if (pieceNumber < 1 || pieceNumber > 7) {
            throw new IllegalArgumentException("Piece number must be in the range 1..7, found: " + pieceNumber);
        }
        return Piece.get(pieceNumber);
    }
}

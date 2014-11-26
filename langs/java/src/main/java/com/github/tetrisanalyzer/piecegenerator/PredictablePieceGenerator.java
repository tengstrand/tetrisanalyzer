package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;

public class PredictablePieceGenerator extends PieceGenerator {
    private int nextPieceIndex;
    private final String pieces;

    public PredictablePieceGenerator(String pieces) {
        this(pieces, 0);
    }

    public PredictablePieceGenerator(String pieces, int nextPieceIndex) {
        super("predictable", "Predetermined sequence of pieces. Returns 'O' when the sequence is exceeded");
        this.pieces = pieces;
        this.nextPieceIndex = nextPieceIndex;
    }

    @Override
    public PredictablePieceGenerator copy() {
        return new PredictablePieceGenerator(pieces, nextPieceIndex);
    }

    @Override
    public String export() {
        return "{ next piece index: " + nextPieceIndex + ", pieces: " + pieces + " }";
    }

    @Override
    public int nextPieceNumber() {
        if (nextPieceIndex < pieces.length()) {
            return Piece.indexOf(pieces.charAt(nextPieceIndex++));
        }
        return Piece.O;
    }
}

package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.PieceSettings;

    public class PredictablePieceGenerator extends PieceGenerator {
    private int pieceIndex = 0;
    private final String pieces;

    public PredictablePieceGenerator(PieceSettings settings, String pieces) {
        super(settings);
        this.pieces = pieces;
    }

    @Override
    public PredictablePieceGenerator copy() {
        return new PredictablePieceGenerator(settings, pieces);
    }

    @Override
    public int nextPieceNumber() {
        if (pieceIndex < pieces.length()) {
            return Piece.indexOf(pieces.charAt(pieceIndex++));
        }
        return Piece.O;
    }

    @Override
    public String export() {
        return null;
    }
}

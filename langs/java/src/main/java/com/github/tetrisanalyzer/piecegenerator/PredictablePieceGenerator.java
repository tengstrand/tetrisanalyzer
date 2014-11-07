package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;

import static com.github.tetrisanalyzer.settings.Setting.setting;

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
    public PieceGeneratorSettings settings() {
        return new PieceGeneratorSettings(
                setting("next piece index", nextPieceIndex),
                setting("pieces", pieces));
    }

    @Override
    public int nextPieceNumber() {
        if (nextPieceIndex < pieces.length()) {
            return Piece.indexOf(pieces.charAt(nextPieceIndex++));
        }
        return Piece.O;
    }
}

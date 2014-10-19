package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.PieceSettings;

import static com.github.tetrisanalyzer.settings.Setting.setting;

public class PredictablePieceGenerator extends PieceGenerator {
    private int nextPieceIndex;
    private final String pieces;

    public PredictablePieceGenerator(PieceSettings settings, String pieces) {
        this(settings, pieces, 0);
    }

    public PredictablePieceGenerator(PieceSettings settings, String pieces, int nextPieceIndex) {
        super(settings);
        this.pieces = pieces;
        this.nextPieceIndex = nextPieceIndex;
    }

    @Override
    public PredictablePieceGenerator copy() {
        return new PredictablePieceGenerator(settings, pieces, nextPieceIndex);
    }

    @Override
    public String description() {
        return "Predetermined sequence of pieces. Returns 'O' if exceeded";
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

package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.PieceSettings;

import static com.github.tetrisanalyzer.settings.Setting.setting;

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
    public PieceGeneratorSettings settings() {
        return new PieceGeneratorSettings(
                setting("description", "Generates a predetermined sequence of pieces or piece O"),
                setting("class", this.getClass().getCanonicalName()),
                setting("pieceIndex", pieceIndex),
                setting("pieces", pieces));
    }

    @Override
    public int nextPieceNumber() {
        if (pieceIndex < pieces.length()) {
            return Piece.indexOf(pieces.charAt(pieceIndex++));
        }
        return Piece.O;
    }
}

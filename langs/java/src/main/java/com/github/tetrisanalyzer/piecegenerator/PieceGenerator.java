package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.PieceSettings;

import static com.github.tetrisanalyzer.settings.Setting.setting;

public abstract class PieceGenerator {
    private Piece[] validPieces;
    public final PieceSettings settings;

    public abstract PieceGenerator copy();
    public abstract PieceGeneratorSettings settings();
    public abstract int nextPieceNumber();

    public abstract String state();
    public abstract String description();

    protected PieceGenerator(PieceSettings settings) {
        this.settings = settings;
        validPieces = Piece.validPieces(settings);
    }

    public Piece nextPiece() {
        int pieceNumber = nextPieceNumber();

        if (pieceNumber < 1 || pieceNumber > 7) {
            throw new IllegalArgumentException("Piece number must be in the range 1..7, found: " + pieceNumber);
        }
        return validPieces[pieceNumber];
    }

    public String export() {
        return new PieceGeneratorSettings(
                setting("description", description()),
                setting("class", this.getClass().getCanonicalName())).add(settings()).export();
    }

    @Override
    public String toString() {
        return export();
    }
}

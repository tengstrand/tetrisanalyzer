package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.PieceSettings;
import com.github.tetrisanalyzer.settings.SettingsReader;

import java.util.Map;

public abstract class PieceGenerator {
    public final String id;
    public final String description;

    public abstract PieceGenerator copy();
    public abstract String export();
    public abstract int nextPieceNumber();

    protected PieceGenerator(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public Piece nextPiece(PieceSettings settings) {
        int pieceNumber = nextPieceNumber();

        if (pieceNumber < 1 || pieceNumber > 7) {
            throw new IllegalArgumentException("Piece number must be in the range 1..7, found: " + pieceNumber);
        }
        return settings.piece(pieceNumber);
    }

    public static String readString(Map settings, String key) {
        return new SettingsReader(settings, "piece generators").readString(key);
    }

    @Override
    public String toString() {
        return export();
    }
}

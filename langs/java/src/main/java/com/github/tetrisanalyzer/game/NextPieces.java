package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.settings.PieceSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NextPieces {
    public final int level;
    public final int knownPieces;
    public List<Piece> pieces;

    private final PieceGenerator pieceGenerator;
    private final PieceSettings pieceSettings;

    public NextPieces(PieceGenerator pieceGenerator, PieceSettings pieceSettings, int level, int knownPieces, List<Piece> pieces) {
        this.level = level;
        this.knownPieces = knownPieces;
        this.pieceGenerator = pieceGenerator;
        this.pieceSettings = pieceSettings;

        this.pieces = pieces == null ? new ArrayList<>() : new ArrayList<>(pieces);

        for (int i=this.pieces.size(); i<knownPieces; i++) {
            Piece piece = pieceGenerator.nextPiece(pieceSettings);
            this.pieces.add(piece);
        }
    }

    public boolean isUnknown() {
        return knownPieces == 0;
    }

    public Piece piece() {
        return pieces.get(0);
    }

    /**
     * Get another piece from the piece generator. Stay on the same level.
     */
    public NextPieces next() {
        List<Piece> pieces = this.pieces.subList(1, this.pieces.size());
        pieces.add(pieceGenerator.nextPiece(pieceSettings));
        return new NextPieces(pieceGenerator, pieceSettings, level, knownPieces, pieces);
    }

    /**
     * Decrease the level and consume one piece fron the piece queue.
     */
    public NextPieces nextLevel() {
        int known = knownPieces == 0 ? 0 : knownPieces - 1;
        return new NextPieces(pieceGenerator, pieceSettings, level-1, known, pieces.subList(1, pieces.size()));
    }

    public NextPieces current(Piece piece) {
        return new NextPieces(pieceGenerator, pieceSettings, level, knownPieces, Arrays.asList(piece));
    }

    @Override
    public String toString() {
        return "level: " + level + "\n" +
                "knownPieces: " + knownPieces + "\n" +
                "pieces: " + pieces;
    }
}

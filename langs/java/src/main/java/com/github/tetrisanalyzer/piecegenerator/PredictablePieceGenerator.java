package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piece.PieceO;
import com.github.tetrisanalyzer.settings.PieceSettings;

import java.util.Arrays;
import java.util.Iterator;

public class PredictablePieceGenerator extends PieceGenerator {
    private final int pieceO;
    private final Iterator<Piece> pieceSequenceIterator;

    public PredictablePieceGenerator(PieceSettings settings, Piece... pieces) {
        super(settings);
        this.pieceO = new PieceO(settings).number();
        this.pieceSequenceIterator = Arrays.asList(pieces).iterator();
    }

    @Override
    public int nextPieceNumber() {
        if (pieceSequenceIterator.hasNext()) {
            return pieceSequenceIterator.next().number();
        }
        return pieceO;
    }
}

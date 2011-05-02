package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piece.PieceO;

import java.util.Iterator;
import java.util.List;

public class PredictablePieceGenerator extends PieceGenerator {
    private Iterator<Piece> pieceSequenceIterator;

    public PredictablePieceGenerator(List<Piece> pieceSequence) {
        this.pieceSequenceIterator = pieceSequence.iterator();
    }

    @Override
    public int nextPieceNumber() {
        if (pieceSequenceIterator.hasNext()) {
            return pieceSequenceIterator.next().number();
        }
        return new PieceO().number();
    }
}

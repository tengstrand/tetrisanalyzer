package nu.tengstrand.tetrisanalyzer.piecegenerator;

import nu.tengstrand.tetrisanalyzer.piece.Piece;
import nu.tengstrand.tetrisanalyzer.piece.PieceO;

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

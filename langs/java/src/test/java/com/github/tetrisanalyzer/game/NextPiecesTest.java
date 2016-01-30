package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.piecegenerator.PredictablePieceGenerator;
import com.github.tetrisanalyzer.settings.PieceSettings;
import com.github.tetrisanalyzer.settings.StandardGameSettings;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NextPiecesTest {

    @Test
    public void pickPiecesInOrder_fromListOfPieces() {
        assertEquals("I", piece(nextPieces()));
        assertEquals("S", piece(nextPieces().next()));
        assertEquals("Z", piece(nextPieces().next().next()));
        assertEquals("L", piece(nextPieces().next().next().next()));
        assertEquals("J", piece(nextPieces().next().next().next().next()));
    }

    private NextPieces nextPieces() {
        PieceSettings settings = new StandardGameSettings(10);
        PieceGenerator pieceGenerator = new PredictablePieceGenerator("O");
        Piece[] p = Piece.pieces(settings);
        List<Piece> pieces = Arrays.asList(p[1], p[2], p[3], p[4], p[5]);
        return new NextPieces(pieceGenerator, settings, 2, 1, pieces);
    }

    @Test
    public void pickPiecesInOrder_fromPieceGenerator() {
        assertEquals("T", piece(nextPiecesInOrder()));
        assertEquals("L", piece(nextPiecesInOrder().next()));
        assertEquals("J", piece(nextPiecesInOrder().next().next()));
        assertEquals("S", piece(nextPiecesInOrder().next().next().next()));
        assertEquals("O", piece(nextPiecesInOrder().next().next().next().next()));
        assertEquals("I", piece(nextPiecesInOrder().next().next().next().next().next()));
        assertEquals("Z", piece(nextPiecesInOrder().next().next().next().next().next().next()));
    }

    private NextPieces nextPiecesInOrder() {
        PieceSettings settings = new StandardGameSettings(10);
        PieceGenerator pieceGenerator = new PredictablePieceGenerator("TLJSOIZ");
        return new NextPieces(pieceGenerator, settings, 2, 1, null);
    }

    private String piece(NextPieces nextPieces) {
        return String.valueOf(nextPieces.piece().character());
    }
}
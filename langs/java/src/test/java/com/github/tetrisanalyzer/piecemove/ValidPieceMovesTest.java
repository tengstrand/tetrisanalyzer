package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import com.github.tetrisanalyzer.settings.CustomGameSettings;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.StandardGameSettings;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.github.tetrisanalyzer.piece.Piece.createPieceI;
import static com.github.tetrisanalyzer.piece.Piece.createPieceO;
import static com.github.tetrisanalyzer.piece.Piece.createPieceS;
import static com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator.calculate;
import static org.junit.Assert.assertEquals;

public class ValidPieceMovesTest {

    @Test
    public void startPieceI_StandardSettings() {
        Board board = Board.create(10, 20);
        GameSettings settings = new StandardGameSettings(board);
        Piece piece = createPieceI(settings);
        ValidPieceMoves validPieceMoves = new ValidPieceMoves(board, piece, settings);
        PieceMove startMove = validPieceMoves.calculateStartMove(board.width, board.height);

        assertEquals(new Move(0, 3, 1), startMove.move);
    }

    @Test
    public void startPieceS_StandardSettings() {
        Board board = Board.create(10, 20);
        GameSettings settings = new StandardGameSettings(board);
        Piece piece = createPieceS(settings);
        ValidPieceMoves validPieceMoves = new ValidPieceMoves(board, piece, settings);
        PieceMove startMove = validPieceMoves.calculateStartMove(board.width, board.height);

        assertEquals(new Move(0,4, 1), startMove.move);
    }

    @Test
    public void starPieceS_adjustPieceStartYOneStepDown() {
        Board board = Board.create(10, 20);
        GameSettings settings = new TestGameSettings(board);
        Piece piece = createPieceS(settings);
        ValidPieceMoves validPieceMoves = new ValidPieceMoves(board, piece, settings);
        PieceMove startMove = validPieceMoves.calculateStartMove(board.width, board.height);

        assertEquals(new Move(0,4, 2), startMove.move);
    }

    @Test
    public void startPieceI_AtariSettings() {
        Board board = Board.create(10, 20);
        GameSettings settings = new AtariGameSettings(board);
        Piece piece = createPieceI(settings);
        ValidPieceMoves validPieceMoves = new ValidPieceMoves(board, piece, settings);
        PieceMove startMove = validPieceMoves.calculateStartMove(board.width, board.height);

        assertEquals(new Move(0,4, 1), startMove.move);
    }

    @Test
    public void startPieceS_AtariSettings() {
        Board board = Board.create(10, 20);
        GameSettings settings = new AtariGameSettings(board);
        Piece piece = createPieceS(settings);
        ValidPieceMoves validPieceMoves = new ValidPieceMoves(board, piece, settings);
        PieceMove startMove = validPieceMoves.calculateStartMove(board.width, board.height);

        assertEquals(new Move(0,4, 0), startMove.move);
    }

    @Test
    public void validMoves_AtariSettings_slidingOff() {
        Board board = Board.create(5, 5);
        GameSettings settings = new AtariGameSettings(board);
        Piece piece = createPieceS(settings);
        ValidPieceMoves validPieceMoves = new ValidPieceMoves(board, piece, settings);

        Set<PieceMove> pieceMoves = new LinkedHashSet<>();
        addPieceMoves(validPieceMoves.calculateStartMove(board.width, board.height), pieceMoves);

        Set<PieceMove> expectedMoves = new LinkedHashSet<>();
        expectedMoves.add(new PieceMove(piece, new Move(0,1, 0)));
        expectedMoves.add(new PieceMove(piece, new Move(0,0, 0)));
        expectedMoves.add(new PieceMove(piece, new Move(0,0, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(0,0, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(0,0, 3)));
        expectedMoves.add(new PieceMove(piece, new Move(1,1, 0)));
        expectedMoves.add(new PieceMove(piece, new Move(1,0, 0)));
        expectedMoves.add(new PieceMove(piece, new Move(1,0, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(1,0, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(1,2, 0)));
        expectedMoves.add(new PieceMove(piece, new Move(1,3, 0)));
        expectedMoves.add(new PieceMove(piece, new Move(1,3, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(1,3, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(1,2, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(1,2, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(1,1, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(1,1, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(0,2, 0)));
        expectedMoves.add(new PieceMove(piece, new Move(0,2, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(0,2, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(0,2, 3)));
        expectedMoves.add(new PieceMove(piece, new Move(0,1, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(0,1, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(0,1, 3)));

        assertEquals(expectedMoves, pieceMoves);
    }

    @Test
    public void validMoves_StandardTetris_slidingOff() {
        Board board = Board.create(5, 5);
        GameSettings settings = new StandardGameSettings(board);
        Piece piece = createPieceS(settings);
        ValidPieceMoves validPieceMoves = new ValidPieceMoves(board, piece, settings);

        Set<PieceMove> pieceMoves = new LinkedHashSet<>();
        addPieceMoves(validPieceMoves.calculateStartMove(board.width, board.height), pieceMoves);

        Set<PieceMove> expectedMoves = new LinkedHashSet<>();
        expectedMoves.add(new PieceMove(piece, new Move(0,1, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(1,2, 0)));
        expectedMoves.add(new PieceMove(piece, new Move(1,3, 0)));
        expectedMoves.add(new PieceMove(piece, new Move(1,3, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(1,2, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(1,1, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(1,0, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(1,0, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(1,1, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(1,2, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(1,3, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(1,1, 0)));
        expectedMoves.add(new PieceMove(piece, new Move(1,0, 0)));
        expectedMoves.add(new PieceMove(piece, new Move(0,0, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(0,0, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(0,0, 3)));
        expectedMoves.add(new PieceMove(piece, new Move(0,2, 1)));
        expectedMoves.add(new PieceMove(piece, new Move(0,2, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(0,2, 3)));
        expectedMoves.add(new PieceMove(piece, new Move(0,1, 2)));
        expectedMoves.add(new PieceMove(piece, new Move(0,1, 3)));

        assertEquals(expectedMoves, pieceMoves);
    }

    private void addPieceMoves(PieceMove pieceMove, Set<PieceMove> pieceMoves) {
        if (pieceMove != null && !pieceMoves.contains(pieceMove)) {
            pieceMoves.add(pieceMove);
            for (PieceMove move : pieceMove.asideAndRotateMoves()) {
                addPieceMoves(move, pieceMoves);
            }
            addPieceMoves(pieceMove.down, pieceMoves);
        }
    }

    public static class TestGameSettings extends CustomGameSettings {

        public TestGameSettings(Board board) {
            super("", "", "", 3, 0, false, false, null, new Adjustments[] {
                    calculate("-", dxdy(0,0)),
                    calculate("O", dxdy(1,1)),
                    calculate("I", dxdy(0,1), dxdy(2,0)),
                    // "standing" S has its starting y = -1
                    calculate("S", dxdy(1,1), dxdy(2,-1)),
                    calculate("Z", dxdy(1,1), dxdy(2,0)),
                    calculate("L", dxdy(1,1), dxdy(2,0), dxdy(1,0), dxdy(1,0)),
                    calculate("J", dxdy(1,1), dxdy(2,0), dxdy(1,0), dxdy(1,0)),
                    calculate("T", dxdy(1, 1), dxdy(2,0), dxdy(1,0), dxdy(1,0)),
                    calculate("x", dxdy(0,0)),
                    calculate("+", dxdy(0,0))});
        }
/*
        @Override
        public int pieceStartY() {
            return pieceStartY;
        }
*/
    }

    @Test
    public void getStartMove() {
        Board board = Board.create(6, 5);
        GameSettings settings = new AtariGameSettings(board);
        Piece piece = createPieceO(settings);
        ValidPieceMoves validPieceMoves = new ValidPieceMoves(board, piece, settings);
        PieceMove startMove = validPieceMoves.calculateStartMove(board.width, board.height);

        assertEquals(new PieceMove(piece, new Move(0,1, 0)), startMove);

        Set<PieceMove> expectedMoves = new HashSet<>();
        expectedMoves.add(new PieceMove(piece, new Move(0,0, 0)));
        expectedMoves.add(new PieceMove(piece, new Move(0,2, 0)));

        assertEquals(expectedMoves, startMove.asideAndRotateMoves());

        assertEquals(new PieceMove(piece, new Move(0,1, 1)), startMove.down);
    }
}

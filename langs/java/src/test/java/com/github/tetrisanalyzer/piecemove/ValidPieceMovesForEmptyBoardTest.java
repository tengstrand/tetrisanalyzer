package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piece.PieceS;
import com.github.tetrisanalyzer.settings.TetrisAnalyzerGameSettings;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.StandardGameSettings;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ValidPieceMovesForEmptyBoardTest {

    @Test
    public void startMove() {
        GameSettings settings = new TetrisAnalyzerGameSettings(5, 5);
        Board board = new Board(settings.boardWidth(), settings.boardWidth());
        Piece piece = new PieceS(settings);
        ValidPieceMovesForEmptyBoard validPieceMovesForEmptyBoard = new ValidPieceMovesForEmptyBoard(board, piece, settings);

        Set<PieceMove> pieceMoves = new LinkedHashSet<PieceMove>();
        addPieceMoves(validPieceMovesForEmptyBoard.getStartMove(), pieceMoves);

        Set<PieceMove> expectedMoves = new LinkedHashSet<PieceMove>();
        expectedMoves.add(new PieceMove(board, piece, new Move(0,1, 0)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,0, 0)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,0, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,0, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,0, 3)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,1, 0)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,0, 0)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,0, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,0, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,2, 0)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,3, 0)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,3, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,3, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,2, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,2, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,1, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,1, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,2, 0)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,2, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,2, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,2, 3)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,1, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,1, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,1, 3)));

        assertEquals(expectedMoves, pieceMoves);
    }

    @Test
    public void startMoveStandardTetris() {
        GameSettings settings = new StandardGameSettings(5, 5);
        Board board = new Board(settings.boardWidth(), settings.boardWidth());
        Piece piece = new PieceS(settings);
        ValidPieceMovesForEmptyBoard validPieceMovesForEmptyBoard = new ValidPieceMovesForEmptyBoard(board, piece, settings);

        Set<PieceMove> pieceMoves = new LinkedHashSet<PieceMove>();
        addPieceMoves(validPieceMovesForEmptyBoard.getStartMove(), pieceMoves);

        Set<PieceMove> expectedMoves = new LinkedHashSet<PieceMove>();
        expectedMoves.add(new PieceMove(board, piece, new Move(0,1, 0)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,2, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,3, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,3, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,2, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,1, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,0, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,1, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,2, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,2, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,1, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,0, 2)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,0, 3)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,1, 3)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,2, 3)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,0, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,1, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(1,0, 1)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,0, 0)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,2, 0)));

        assertEquals(expectedMoves, pieceMoves);
    }

    private void addPieceMoves(PieceMove pieceMove, Set<PieceMove> pieceMoves) {
        if (pieceMove != null && !pieceMoves.contains(pieceMove)) {
            pieceMoves.add(pieceMove);
            for (PieceMove move : pieceMove.getAsideAndRotateMoves()) {
                addPieceMoves(move, pieceMoves);
            }
            addPieceMoves(pieceMove.down, pieceMoves);
        }
    }
}

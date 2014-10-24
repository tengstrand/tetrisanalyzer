package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.StandardGameSettings;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.github.tetrisanalyzer.board.Board.createBoard;
import static com.github.tetrisanalyzer.piece.Piece.createPieceI;
import static com.github.tetrisanalyzer.piece.Piece.createPieceS;
import static org.junit.Assert.assertEquals;

public class ValidPieceMovesForEmptyBoardTest {

    @Test
    public void startPieceIUsingStandardSettings() {
        Board board = createBoard(10, 20);
        GameSettings settings = new StandardGameSettings(board);
        Piece piece = createPieceI(settings);
        ValidPieceMovesForEmptyBoard validPieceMovesForEmptyBoard = new ValidPieceMovesForEmptyBoard(board, piece, settings);
        PieceMove startMove = validPieceMovesForEmptyBoard.getStartMove();

        assertEquals(new Move(0, 3, 0), startMove.move);
    }

    @Test
    public void startPieceSUsingStandardSettings() {
        Board board = createBoard(10, 20);
        GameSettings settings = new StandardGameSettings(board);
        Piece piece = createPieceS(settings);
        ValidPieceMovesForEmptyBoard validPieceMovesForEmptyBoard = new ValidPieceMovesForEmptyBoard(board, piece, settings);
        PieceMove startMove = validPieceMovesForEmptyBoard.getStartMove();

        assertEquals(new Move(0,4, 0), startMove.move);
    }

    @Test
    public void startPieceIUsingAtariSettings() {
        Board board = createBoard(10, 20);
        GameSettings settings = new AtariGameSettings(board);
        Piece piece = createPieceI(settings);
        ValidPieceMovesForEmptyBoard validPieceMovesForEmptyBoard = new ValidPieceMovesForEmptyBoard(board, piece, settings);
        PieceMove startMove = validPieceMovesForEmptyBoard.getStartMove();

        assertEquals(new Move(0,4, 1), startMove.move);
    }

    @Test
    public void startPieceSUsingAtariSettings() {
        Board board = createBoard(10, 20);
        GameSettings settings = new AtariGameSettings(board);
        Piece piece = createPieceS(settings);
        ValidPieceMovesForEmptyBoard validPieceMovesForEmptyBoard = new ValidPieceMovesForEmptyBoard(board, piece, settings);
        PieceMove startMove = validPieceMovesForEmptyBoard.getStartMove();

        assertEquals(new Move(0,4, 0), startMove.move);
    }

    @Test
    public void validMovesUsingAtariSettingsSlidingOff() {
        Board board = createBoard(5, 5);
        GameSettings settings = new AtariGameSettings(board);
        Piece piece = createPieceS(settings);
        ValidPieceMovesForEmptyBoard validPieceMovesForEmptyBoard = new ValidPieceMovesForEmptyBoard(board, piece, settings);

        Set<PieceMove> pieceMoves = new LinkedHashSet<>();
        addPieceMoves(validPieceMovesForEmptyBoard.getStartMove(), pieceMoves);

        Set<PieceMove> expectedMoves = new LinkedHashSet<>();
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
    public void validMovesStandardTetris() {
        Board board = createBoard(5, 5);
        GameSettings settings = new StandardGameSettings(board);
        Piece piece = createPieceS(settings);
        ValidPieceMovesForEmptyBoard validPieceMovesForEmptyBoard = new ValidPieceMovesForEmptyBoard(board, piece, settings);

        Set<PieceMove> pieceMoves = new LinkedHashSet<>();
        addPieceMoves(validPieceMovesForEmptyBoard.getStartMove(), pieceMoves);

        Set<PieceMove> expectedMoves = new LinkedHashSet<>();
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
            for (PieceMove move : pieceMove.asideAndRotateMoves()) {
                addPieceMoves(move, pieceMoves);
            }
            addPieceMoves(pieceMove.down, pieceMoves);
        }
    }
}

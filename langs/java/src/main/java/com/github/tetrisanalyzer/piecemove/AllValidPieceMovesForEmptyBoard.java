package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.GameSettings;

import java.util.HashMap;
import java.util.Map;

import static com.github.tetrisanalyzer.piece.Piece.*;

/**
 * Is a holder for all valid moves on an empty board.
 */
public class AllValidPieceMovesForEmptyBoard {
    private final Board board;
    private final Map<Piece, PieceMove> startPieces = new HashMap<Piece, PieceMove>();

    public AllValidPieceMovesForEmptyBoard(Board board, GameSettings settings) {
        this.board = board;

        startPieces.put(createPieceO(settings), new ValidPieceMovesForEmptyBoard(board, createPieceO(settings), settings).getStartMove());
        startPieces.put(createPieceI(settings), new ValidPieceMovesForEmptyBoard(board, createPieceI(settings), settings).getStartMove());
        startPieces.put(createPieceS(settings), new ValidPieceMovesForEmptyBoard(board, createPieceS(settings), settings).getStartMove());
        startPieces.put(createPieceZ(settings), new ValidPieceMovesForEmptyBoard(board, createPieceZ(settings), settings).getStartMove());
        startPieces.put(createPieceL(settings), new ValidPieceMovesForEmptyBoard(board, createPieceL(settings), settings).getStartMove());
        startPieces.put(createPieceJ(settings), new ValidPieceMovesForEmptyBoard(board, createPieceJ(settings), settings).getStartMove());
        startPieces.put(createPieceT(settings), new ValidPieceMovesForEmptyBoard(board, createPieceT(settings), settings).getStartMove());
    }

    public PieceMove startMoveForPiece(Piece piece) {
        if (!startPieces.containsKey(piece)) {
            throw new IllegalStateException("Could not find a start move for piece " + piece + " on board " + board);
        }
        return startPieces.get(piece);
    }
}

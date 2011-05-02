package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piece.*;
import com.github.tetrisanalyzer.settings.GameSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Is a holder for all valid moves on an empty board.
 */
public class AllValidPieceMovesForEmptyBoard {
    private Board board;
    private GameSettings settings;
    private Map<Piece, PieceMove> startPieces = new HashMap<Piece, PieceMove>();

    public AllValidPieceMovesForEmptyBoard(Board board, GameSettings settings) {
        this.board = board;
        this.settings = settings;

        startPieces.put(new PieceI(), new ValidPieceMovesForEmptyBoard(board, new PieceI(), settings).getStartMove());
        startPieces.put(new PieceZ(), new ValidPieceMovesForEmptyBoard(board, new PieceZ(), settings).getStartMove());
        startPieces.put(new PieceS(), new ValidPieceMovesForEmptyBoard(board, new PieceS(), settings).getStartMove());
        startPieces.put(new PieceJ(), new ValidPieceMovesForEmptyBoard(board, new PieceJ(), settings).getStartMove());
        startPieces.put(new PieceL(), new ValidPieceMovesForEmptyBoard(board, new PieceL(), settings).getStartMove());
        startPieces.put(new PieceT(), new ValidPieceMovesForEmptyBoard(board, new PieceT(), settings).getStartMove());
        startPieces.put(new PieceO(), new ValidPieceMovesForEmptyBoard(board, new PieceO(), settings).getStartMove());
    }

    public PieceMove startMoveForPiece(Piece piece) {
        if (!startPieces.containsKey(piece)) {
            throw new IllegalStateException("Could not find a start move for piece " + piece + " on board " + board);
        }
        return startPieces.get(piece);
    }
}

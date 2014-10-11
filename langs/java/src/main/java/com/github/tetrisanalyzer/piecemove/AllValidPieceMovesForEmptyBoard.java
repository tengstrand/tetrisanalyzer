package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piece.PieceI;
import com.github.tetrisanalyzer.piece.PieceJ;
import com.github.tetrisanalyzer.piece.PieceL;
import com.github.tetrisanalyzer.piece.PieceO;
import com.github.tetrisanalyzer.piece.PieceS;
import com.github.tetrisanalyzer.piece.PieceT;
import com.github.tetrisanalyzer.piece.PieceZ;
import com.github.tetrisanalyzer.settings.GameSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Is a holder for all valid moves on an empty board.
 */
public class AllValidPieceMovesForEmptyBoard {
    private final Board board;
    private final Map<Piece, PieceMove> startPieces = new HashMap<Piece, PieceMove>();

    public AllValidPieceMovesForEmptyBoard(Board board, GameSettings settings) {
        this.board = board;

        startPieces.put(new PieceI(settings), new ValidPieceMovesForEmptyBoard(board, new PieceI(settings), settings).getStartMove());
        startPieces.put(new PieceZ(settings), new ValidPieceMovesForEmptyBoard(board, new PieceZ(settings), settings).getStartMove());
        startPieces.put(new PieceS(settings), new ValidPieceMovesForEmptyBoard(board, new PieceS(settings), settings).getStartMove());
        startPieces.put(new PieceJ(settings), new ValidPieceMovesForEmptyBoard(board, new PieceJ(settings), settings).getStartMove());
        startPieces.put(new PieceL(settings), new ValidPieceMovesForEmptyBoard(board, new PieceL(settings), settings).getStartMove());
        startPieces.put(new PieceT(settings), new ValidPieceMovesForEmptyBoard(board, new PieceT(settings), settings).getStartMove());
        startPieces.put(new PieceO(settings), new ValidPieceMovesForEmptyBoard(board, new PieceO(settings), settings).getStartMove());
    }

    public PieceMove startMoveForPiece(Piece piece) {
        if (!startPieces.containsKey(piece)) {
            throw new IllegalStateException("Could not find a start move for piece " + piece + " on board " + board);
        }
        return startPieces.get(piece);
    }
}

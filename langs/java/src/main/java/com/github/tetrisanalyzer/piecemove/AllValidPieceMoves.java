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
public class AllValidPieceMoves {
    private final Map<Piece, PieceMove> startPieces = new HashMap<Piece, PieceMove>();

    public AllValidPieceMoves(Board board, GameSettings settings) {
        startPieces.put(createPieceO(settings), new ValidPieceMoves(board, createPieceO(settings), settings).calculateStartMove(board));
        startPieces.put(createPieceI(settings), new ValidPieceMoves(board, createPieceI(settings), settings).calculateStartMove(board));
        startPieces.put(createPieceS(settings), new ValidPieceMoves(board, createPieceS(settings), settings).calculateStartMove(board));
        startPieces.put(createPieceZ(settings), new ValidPieceMoves(board, createPieceZ(settings), settings).calculateStartMove(board));
        startPieces.put(createPieceL(settings), new ValidPieceMoves(board, createPieceL(settings), settings).calculateStartMove(board));
        startPieces.put(createPieceJ(settings), new ValidPieceMoves(board, createPieceJ(settings), settings).calculateStartMove(board));
        startPieces.put(createPieceT(settings), new ValidPieceMoves(board, createPieceT(settings), settings).calculateStartMove(board));
    }

    public PieceMove startMoveForPiece(Piece piece, Board board) {
        if (!startPieces.containsKey(piece)) {
            throw new IllegalStateException("Could not find a start move for piece " + piece + " on board " + board);
        }
        return startPieces.get(piece);
    }
}

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
    private final PieceMove[] pieceMoves = new PieceMove[7];

    public AllValidPieceMoves(Board board, GameSettings settings) {
        startPieces.put(createPieceO(settings), pieceMoves[0] = new ValidPieceMoves(board, createPieceO(settings), settings).calculateStartMove(board));
        startPieces.put(createPieceI(settings), pieceMoves[1] = new ValidPieceMoves(board, createPieceI(settings), settings).calculateStartMove(board));
        startPieces.put(createPieceS(settings), pieceMoves[2] = new ValidPieceMoves(board, createPieceS(settings), settings).calculateStartMove(board));
        startPieces.put(createPieceZ(settings), pieceMoves[3] = new ValidPieceMoves(board, createPieceZ(settings), settings).calculateStartMove(board));
        startPieces.put(createPieceL(settings), pieceMoves[4] = new ValidPieceMoves(board, createPieceL(settings), settings).calculateStartMove(board));
        startPieces.put(createPieceJ(settings), pieceMoves[5] = new ValidPieceMoves(board, createPieceJ(settings), settings).calculateStartMove(board));
        startPieces.put(createPieceT(settings), pieceMoves[6] = new ValidPieceMoves(board, createPieceT(settings), settings).calculateStartMove(board));
    }

    public PieceMove startMoveForPiece(Piece piece) {
        if (!startPieces.containsKey(piece)) {
            throw new IllegalStateException("Could not find a start move for piece " + piece);
        }
        return startPieces.get(piece);
    }

    public double adjustEquityIfOccupiedStartPiece(double equity, double maxEquity, Board board) {
        int cnt = 0;

        for (int p=0; p<7; p++) {
            if (!pieceMoves[p].isFree(board)) {
                cnt++;
            }
        }
        if (cnt == 0) {
            return equity;
        }
        if (cnt == 7) {
            return maxEquity;
        }
        return ((7-cnt) * equity + cnt * maxEquity) / 7;
    }
}

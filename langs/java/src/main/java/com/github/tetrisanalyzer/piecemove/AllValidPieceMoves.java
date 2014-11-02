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
        this(board.width, board.height, settings);
    }

    public AllValidPieceMoves(int boardWidth, int boardHeight, GameSettings settings) {
        startPieces.put(createPieceO(settings), pieceMoves[0] = new ValidPieceMoves(boardWidth, boardHeight, createPieceO(settings), settings).calculateStartMove(boardWidth, boardHeight));
        startPieces.put(createPieceI(settings), pieceMoves[1] = new ValidPieceMoves(boardWidth, boardHeight, createPieceI(settings), settings).calculateStartMove(boardWidth, boardHeight));
        startPieces.put(createPieceS(settings), pieceMoves[2] = new ValidPieceMoves(boardWidth, boardHeight, createPieceS(settings), settings).calculateStartMove(boardWidth, boardHeight));
        startPieces.put(createPieceZ(settings), pieceMoves[3] = new ValidPieceMoves(boardWidth, boardHeight, createPieceZ(settings), settings).calculateStartMove(boardWidth, boardHeight));
        startPieces.put(createPieceL(settings), pieceMoves[4] = new ValidPieceMoves(boardWidth, boardHeight, createPieceL(settings), settings).calculateStartMove(boardWidth, boardHeight));
        startPieces.put(createPieceJ(settings), pieceMoves[5] = new ValidPieceMoves(boardWidth, boardHeight, createPieceJ(settings), settings).calculateStartMove(boardWidth, boardHeight));
        startPieces.put(createPieceT(settings), pieceMoves[6] = new ValidPieceMoves(boardWidth, boardHeight, createPieceT(settings), settings).calculateStartMove(boardWidth, boardHeight));
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

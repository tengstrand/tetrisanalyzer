package com.github.tetrisanalyzer.move;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piece.PieceS;
import com.github.tetrisanalyzer.piecemove.PieceMove;
import com.github.tetrisanalyzer.piecemove.ValidPieceMovesForEmptyBoard;
import com.github.tetrisanalyzer.settings.TetrisAnalyzerGameSettings;
import com.github.tetrisanalyzer.settings.GameSettings;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EvaluatedMovesTest {
    Board board = new Board();
    Piece piece = new PieceS(new TetrisAnalyzerGameSettings());

    @Test
    public void bestMove() {
        assertEquals(new PieceMove(board, piece, new Move(0,7, 18)), getEvaluatedMoves().bestMove());
    }

    private EvaluatedMoves getEvaluatedMoves() {
        GameSettings settings = new TetrisAnalyzerGameSettings();
        PieceMove startPieceMove = new ValidPieceMovesForEmptyBoard(board, piece, settings).getStartMove();
        List<PieceMove> validMoves = new ValidMoves(board).getPieceMoves(startPieceMove);
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1();
        return new EvaluatedMoves(validMoves, boardEvaluator);
    }

    @Test
    public void evaluatedMoves() {
        List<MoveEquity> evaluatedMoves = getEvaluatedMoves().moves;
        Collections.sort(evaluatedMoves, new Comparator<MoveEquity>() {
            public int compare(MoveEquity m1, MoveEquity m2) {
                if (m1.equity < m2.equity) {
                    return -1;
                }
                return m1.equity > m2.equity ? 1 : 0;
            }
        });
        List<MoveEquity> moves = new ArrayList<MoveEquity>();
        for (MoveEquity moveEquity : evaluatedMoves) {
            moves.add(roundThreeDecimals(moveEquity));
        }
        List<MoveEquity> expectedMoves = new ArrayList<MoveEquity>();
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(0,7, 18)), 0.000));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(1,0, 17)), 0.755));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(0,0, 18)), 2.504));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(0,5, 18)), 3.374));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(0,2, 18)), 3.755));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(1,8, 17)), 3.777));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(0,3, 18)), 3.855));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(0,1, 18)), 4.115));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(0,4, 18)), 4.389));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(0,6, 18)), 5.323));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(1,2, 17)), 7.052));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(1,6, 17)), 7.265));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(1,4, 17)), 7.308));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(1,5, 17)), 8.079));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(1,7, 17)), 8.319));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(1,3, 17)), 9.075));
        expectedMoves.add(new MoveEquity(new PieceMove(board, piece, new Move(1,1, 17)), 10.824));
        assertEquals(expectedMoves, moves);
    }

    private MoveEquity roundThreeDecimals(MoveEquity moveEquity) {
        double equity = Math.round((moveEquity.equity - 12.43) * 1000) / 1000.0;
        return new MoveEquity(moveEquity.pieceMove, equity);
    }
}

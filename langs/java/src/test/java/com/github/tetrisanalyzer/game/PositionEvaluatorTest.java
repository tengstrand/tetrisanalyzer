package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator12;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.move.MoveEquity;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.piecegenerator.PredictablePieceGenerator;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.piecemove.PieceMove;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.StandardGameSettings;
import org.junit.Test;

import java.util.*;

import static com.github.tetrisanalyzer.board.Board.create;
import static com.github.tetrisanalyzer.piece.Piece.createPieceS;
import static org.junit.Assert.assertEquals;

public class PositionEvaluatorTest {
    Board board = create();
    Piece piece = createPieceS(new AtariGameSettings());

    @Test
    public void bestMove() {
        List<MoveEquity> evaluatedMoves = getEvaluatedMoves();
        assertEquals(new PieceMove(piece, new Move(0,7, 18)), PositionEvaluator.bestMove(evaluatedMoves).pieceMove);
    }

    private List<MoveEquity> getEvaluatedMoves() {
        GameSettings settings = new AtariGameSettings();
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator12(10, 20, settings);
        AllValidPieceMoves allValidPieceMoves = new AllValidPieceMoves(board, new StandardGameSettings(board));
        PieceGenerator pieceGenerator = new PredictablePieceGenerator("O");
        NextPieces nextPieces = new NextPieces(pieceGenerator, settings, 1, 1, Arrays.asList(piece));
        return PositionEvaluator.evaluate(allValidPieceMoves, Piece.pieces(settings), boardEvaluator, board, nextPieces);
    }

    @Test
    public void evaluatedMoves() {
        List<MoveEquity> evaluatedMoves = getEvaluatedMoves();
        Collections.sort(evaluatedMoves, new Comparator<MoveEquity>() {
            public int compare(MoveEquity m1, MoveEquity m2) {
                if (m1.equity < m2.equity) {
                    return -1;
                }
                return m1.equity > m2.equity ? 1 : 0;
            }
        });
        List<MoveEquity> moves = new ArrayList<>();
        for (MoveEquity moveEquity : evaluatedMoves) {
            moves.add(roundThreeDecimals(moveEquity));
        }
        List<MoveEquity> expectedMoves = new ArrayList<>();

        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(0,7, 18)), 0.000));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(1,0, 17)), 1.188));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(0,0, 18)), 2.867));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(0,5, 18)), 3.362));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(0,2, 18)), 3.704));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(0,3, 18)), 3.81));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(0,4, 18)), 4.282));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(0,1, 18)), 4.474));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(1,8, 17)), 4.642));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(0,6, 18)), 6.188));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(1,2, 17)), 7.582));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(1,6, 17)), 7.735));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(1,4, 17)), 7.852));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(1,5, 17)), 8.508));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(1,3, 17)), 9.452));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(1,7, 17)), 9.689));
        expectedMoves.add(new MoveEquity(new PieceMove(piece, new Move(1,1, 17)), 13.181));

        assertEquals(expectedMoves, moves);
    }

    private MoveEquity roundThreeDecimals(MoveEquity moveEquity) {
        double equity = Math.round((moveEquity.equity - 11.433) * 1000) / 1000.0;
        return new MoveEquity(moveEquity.pieceMove, equity);
    }
}

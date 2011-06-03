package nu.tengstrand.tetrisanalyzer.move;

import nu.tengstrand.tetrisanalyzer.board.Board;
import nu.tengstrand.tetrisanalyzer.boardevaluator.BoardEvaluator;
import nu.tengstrand.tetrisanalyzer.piecemove.PieceMove;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes a list of piece moves and evaluates them using the given board evaluator.
 */
public class EvaluatedMoves {
    List<MoveEquity> moves;
    private List<PieceMove> pieceMoves;
    private BoardEvaluator boardEvaluator;

    public EvaluatedMoves(List<PieceMove> pieceMoves, BoardEvaluator boardEvaluator) {
        this.pieceMoves = pieceMoves;
        this.boardEvaluator = boardEvaluator;
        moves = evaluateValidMoves();
    }

    /**
     * Returns the move (if any) that is ranked as number one.
     */
    public PieceMove bestMove() {
        if (moves.isEmpty()) {
            return null;
        }
        double bestEquity = Double.MAX_VALUE;
        PieceMove bestPieceMove = null;

        for (MoveEquity move : moves) {
            if (move.equity < bestEquity) {
                bestEquity = move.equity;
                bestPieceMove = move.pieceMove;
            }
        }
        return bestPieceMove;
    }

    /**
     * Evaluates the equity of all valid moves for a given position.
     */
    private List<MoveEquity> evaluateValidMoves() {
        List<MoveEquity> evaluatedMoves = new ArrayList<MoveEquity>();
        if (!pieceMoves.isEmpty()) {
            Board boardCopy = pieceMoves.get(0).getBoardCopy();

            for (PieceMove pieceMove : pieceMoves) {
                evaluatedMoves.add(new MoveEquity(pieceMove, evaluate(pieceMove, boardCopy)));
            }
        }
        return evaluatedMoves;
    }

    private double evaluate(PieceMove pieceMove, Board boardCopy) {
        int clearedLines = pieceMove.setPiece();
        double equity = boardEvaluator.evaluate(pieceMove.getBoard());

        if (clearedLines == 0) {
            pieceMove.clearPiece();
        } else {
            pieceMove.getBoard().restore(boardCopy);
        }
        return equity;
    }
}

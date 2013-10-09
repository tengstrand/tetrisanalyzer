package nu.tengstrand.tetrisanalyzer.game;

import nu.tengstrand.tetrisanalyzer.board.Board;
import nu.tengstrand.tetrisanalyzer.boardevaluator.BoardEvaluator;
import nu.tengstrand.tetrisanalyzer.move.EvaluatedMoves;
import nu.tengstrand.tetrisanalyzer.move.ValidMoves;
import nu.tengstrand.tetrisanalyzer.piecegenerator.PieceGenerator;
import nu.tengstrand.tetrisanalyzer.piecemove.AllValidPieceMovesForEmptyBoard;
import nu.tengstrand.tetrisanalyzer.piecemove.PieceMove;
import nu.tengstrand.tetrisanalyzer.settings.GameSettings;

import java.util.List;

/**
 * Plays a game of Tetris using specified board, board evaluator and settings.
 */
public class Game {
    private int clearedLines = 0;
    private AllValidPieceMovesForEmptyBoard allValidPieceMoves;

    private Board board;
    private BoardEvaluator boardEvaluator;
    private PieceGenerator pieceGenerator;
    private GameSettings settings;

    public Game(Board board, BoardEvaluator boardEvaluator, PieceGenerator pieceGenerator, GameSettings settings) {
        this.board = board;
        this.boardEvaluator = boardEvaluator;
        this.pieceGenerator = pieceGenerator;
        this.settings = settings;

        allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);
    }

    public int getClearedLines() {
        return clearedLines;
    }

    /**
     * Plays the specified number of pieces.
     */
    public void play(long maxMoves) {
        long moves = 0;
        PieceMove bestMove = evaluateBestMove();

        while (moves < maxMoves && bestMove != null) {
            moves++;
            clearedLines += bestMove.setPiece();
            bestMove = evaluateBestMove();
        }
    }

    private PieceMove evaluateBestMove() {
        PieceMove startPieceMove = allValidPieceMoves.startMoveForPiece(pieceGenerator.nextPiece());
        List<PieceMove> validMoves = new ValidMoves(board).getPieceMoves(startPieceMove);
        return new EvaluatedMoves(validMoves, boardEvaluator).bestMove();
    }
}


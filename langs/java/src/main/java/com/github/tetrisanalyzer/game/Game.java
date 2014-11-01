package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.move.EvaluatedMoves;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.move.ValidMoves;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMovesForEmptyBoard;
import com.github.tetrisanalyzer.piecemove.PieceMove;
import com.github.tetrisanalyzer.settings.GameSettings;

import java.util.List;

/**
 * Plays a game of Tetris using specified board, board evaluator, piece generator and settings.
 */
public class Game implements Runnable {
    private AllValidPieceMovesForEmptyBoard allValidPieceMoves;

    private int cells;
    public Board board;
    public ColoredBoard coloredBoard;
    private final BoardEvaluator boardEvaluator;
    public final PieceGenerator pieceGenerator;
    private final GameSettings settings;
    private final GameState state;
    public final GameMessenger message;

    public Game(GameState gameState, BoardEvaluator boardEvaluator, GameSettings settings) {
        this.board = gameState.board.copy();
        if (gameState.coloredBoard != null) {
            this.coloredBoard = gameState.coloredBoard.copy();
        }
        this.state = gameState;
        this.message = new GameMessenger(gameState);
        this.boardEvaluator = boardEvaluator;
        this.pieceGenerator = state.pieceGenerator;
        this.settings = settings;
        this.cells = board.numberOfOccupiedCells();

        allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);
    }

    private String board(long moveNo, Piece piece, Move move) {
        if (coloredBoard != null) {
            return coloredBoard.asString(moveNo, piece, move);
        }
        return board.asString(moveNo, piece, move);
    }

    /**
     * Plays a specified number of pieces (state.movesLeft).
     */
    @Override
    public void run() {
        state.duration = Duration.create();
        PieceMove bestMove = null;

        while (state.nonstop || state.movesLeft > 0) {
            Piece piece = pieceGenerator.nextPiece();
            bestMove = evaluateBestMove(piece);
            printBoardEvery10000Piece(state.moves + 1, piece, bestMove.move);
            state.moves++;

            if (!state.nonstop) {
                state.movesLeft--;
            }
            int clearedRows = bestMove.setPiece();
            setPieceOnColoredBoard(bestMove.piece, bestMove.move);
            state.rows += clearedRows;
            cells += 4 - clearedRows * board.width;
            state.cells += cells;
            state.cellDist[cells]++;
            message.setStateIfNeeded(state);
        }
        state.duration = state.duration.stop();

        if (bestMove != null) {
            System.out.println("\n" + board(state.moves + 1, pieceGenerator.copy().nextPiece(), bestMove.move));
        }
        System.out.println();
    }

    private PieceMove evaluateBestMove(Piece piece) {
        PieceMove bestMove = evaluatePiece(piece);

        if (bestMove == null) {
            state.games++;
            state.totalRows += state.rows;
            state.rows = 0;
            board = state.board.copy();
            initColoredBoard();
            cells = board.numberOfOccupiedCells();
            allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);
            bestMove = evaluatePiece(piece);
            if (bestMove == null) {
                throw new IllegalStateException("The starting position is occupied!");
            }
        }
        return bestMove;
    }

    private void printBoardEvery10000Piece(long moveNo, Piece piece, Move move) {
        if ((state.moves % 10000) == 0) {
            System.out.println("\n" + board(moveNo, piece, move));
        }
    }

    private void setPieceOnColoredBoard(Piece piece, Move move) {
        if (coloredBoard != null) {
            coloredBoard.setPiece(piece, move);
        }
    }

    private void initColoredBoard() {
        if (coloredBoard != null) {
            coloredBoard = state.coloredBoard.copy();
        }
    }

    private PieceMove evaluatePiece(Piece piece) {
        PieceMove startPieceMove = allValidPieceMoves.startMoveForPiece(piece);
        List<PieceMove> validMoves = new ValidMoves(board).pieceMoves(startPieceMove);
        return new EvaluatedMoves(validMoves, boardEvaluator).bestMove();
    }
}

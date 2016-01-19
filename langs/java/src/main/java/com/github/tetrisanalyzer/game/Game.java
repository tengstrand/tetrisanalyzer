package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.board.TextBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.move.EvaluatedMoves;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.move.ValidMoves;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.piecemove.PieceMove;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.PieceSettings;

import java.util.List;

/**
 * Plays a game of Tetris using specified board, board evaluator, piece generator and settings.
 */
public class Game implements Runnable {
    private AllValidPieceMoves allValidPieceMoves;

    private boolean stop;
    public boolean stopped;

    public boolean temporarilyPaused;
    public boolean hide;
    public boolean paused;
    public boolean waiting;
    private int numberOfCells;
    public Board board;
    public ColoredBoard coloredBoard;
    private final BoardEvaluator boardEvaluator;
    public final PieceGenerator pieceGenerator;
    public final GameState state;
    public final GameMessage message;
    public final PieceSettings settings;

    public Game(GameState gameState, GameSettings settings, boolean paused, boolean hide) {
        this.board = gameState.board.copy();
        if (gameState.coloredBoard != null) {
            this.coloredBoard = gameState.coloredBoard.copy();
        }
        this.state = gameState;
        this.message = new GameMessage(gameState);
        this.boardEvaluator = gameState.boardEvaluator;
        this.settings = settings;
        this.paused = paused;
        this.hide = hide;
        this.pieceGenerator = state.pieceGenerator;
        this.numberOfCells = board.numberOfOccupiedCells();

        allValidPieceMoves = new AllValidPieceMoves(board, settings);
    }

    private String board(Piece piece, Move move) {
        if (coloredBoard != null) {
            return coloredBoard.asString(piece, move);
        }
        return board.asString(piece, move);
    }

    public void stopThread() {
        stop = true;
    }

    /**
     * Plays a specified number of pieces (state.movesLeft).
     */
    @Override
    public void run() {
        stop = false;

        while (!stop && state.nonstop || state.movesLeft > 0) {
            waitIfPaused();
            Piece piece = pieceGenerator.nextPiece(settings);
            PieceMove bestMove = evaluateBestMove(piece);
            state.totalPieces++;
            state.pieces++;

            if (!state.nonstop) {
                state.movesLeft--;
            }
            setShadowOnColoredBoard(bestMove.piece, bestMove.move);
            message.setStateIfNeeded(state, textBoard(), piece, bestMove == null ? null : bestMove.move);

            int clearedRows = bestMove.setPiece(board);
            setPieceOnColoredBoard(bestMove.piece, bestMove.move);
            state.rows += clearedRows;
            numberOfCells += 4 - clearedRows * board.width;

            state.distribution.increaseArea(numberOfCells);
            state.duration.setEndTime();
            state.rowsPerLastSecond.update(state.duration.endMillis, state.rows + state.totalRows);
            state.piecesPerLastSecond.update(state.duration.endMillis, state.totalPieces);
        }
        stopped = true;
    }

    private void waitIfPaused() {
        if (stop || (!paused && !temporarilyPaused && !hide)) {
            return;
        }
        long pausedAt = System.currentTimeMillis();

        while (!stop && (paused || temporarilyPaused && !hide)) {
            waiting = true;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }
        waiting = false;
        state.duration.adjustPause(pausedAt);
        state.rowsPerLastSecond.idx = 0;
        state.piecesPerLastSecond.idx = 0;
    }

    private TextBoard textBoard() {
        return coloredBoard == null ? board : coloredBoard;
    }

    private PieceMove evaluateBestMove(Piece piece) {
        PieceMove bestMove = evaluatePiece(piece, board);

        if (bestMove == null) {
            state.games++;
            state.totalRows += state.rows;
            setMinRows();
            setMaxRows();
            state.pieces = 0;
            state.rows = 0;
            board = state.startBoard.copy();
            initColoredBoard();
            numberOfCells = board.numberOfOccupiedCells();
            bestMove = evaluatePiece(piece, board);
            if (bestMove == null) {
                throw new IllegalStateException("The starting position is occupied!");
            }
        }
        return bestMove;
    }

    private void setMinRows() {
        if (state.rows < state.minRows) {
            state.minRows = state.rows;
        }
    }

    private void setMaxRows() {
        if (state.rows > state.maxRows) {
            state.maxRows = state.rows;
        }
    }

    private void setPieceOnColoredBoard(Piece piece, Move move) {
        if (coloredBoard != null) {
            coloredBoard.setPiece(piece, move);
        }
    }

    private void setShadowOnColoredBoard(Piece piece, Move move) {
        if (coloredBoard != null) {
            coloredBoard.setShadow(piece, move);
        }
    }

    private void initColoredBoard() {
        coloredBoard = state.coloredStartBoard.copy();
    }

    private PieceMove evaluatePiece(Piece piece, Board board) {
        PieceMove startPieceMove = allValidPieceMoves.startMoveForPiece(piece);
        List<PieceMove> validMoves = new ValidMoves(board).pieceMoves(startPieceMove, board);
        return new EvaluatedMoves(allValidPieceMoves, validMoves, boardEvaluator, board).bestMove();
    }

    public void togglePaused() {
        paused = !paused;
    }

    public void hide() {
        hide = true;
    }
}

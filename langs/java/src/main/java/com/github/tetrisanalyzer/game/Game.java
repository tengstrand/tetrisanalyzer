package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.board.TextBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.move.MoveEquity;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.piecemove.PieceMove;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.PieceSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Plays a game of Tetris using specified board, board evaluator, piece generator and settings.
 */
public class Game implements Runnable {
    private AllValidPieceMoves allValidPieceMoves;

    private boolean stop;
    public boolean stopped;

    private Piece[] pieces;

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
    public NextPieces nextPieces;

    private int numberOfLastBoards = 100;
    private int lastBoardIdx = 0;
    private List<BoardPieceMove> lastBoards = new ArrayList<>();

    public Game(GameState gameState, GameSettings settings, boolean paused, boolean hide) {
        this.board = gameState.board.copy();
        if (gameState.coloredBoard != null) {
            this.coloredBoard = gameState.coloredBoard.copy();
        }
        state = gameState;
        message = new GameMessage(gameState);
        boardEvaluator = gameState.boardEvaluator;
        this.settings = settings;
        pieceGenerator = state.pieceGenerator;

        pieces = Piece.pieces(settings);

        nextPieces = new NextPieces(pieceGenerator, settings, state.level, state.numberOfKnownPieces, nextPiece(state.nextPieces));

        this.paused = paused;
        this.hide = hide;
        numberOfCells = board.numberOfOccupiedCells();

        allValidPieceMoves = new AllValidPieceMoves(board, settings);

        pieces = Piece.pieces(settings);
    }

    private List<Piece> nextPiece(List<String> pieces) {
        List<Piece> result = new ArrayList<>();

        if (pieces != null) {
            for (String p : pieces) {
                Piece piece = this.pieces[Piece.indexOf(p.charAt(0)) - 1];
                result.add(piece);
            }
        }
        return result;
    }

    public String lastBoardsAsString() {
        String result = "";

        for (int i=0; i<numberOfLastBoards; i++) {
            int index = (lastBoardIdx + i) % numberOfLastBoards;
            if (index < lastBoards.size()) {
                result += lastBoards.get(index).toString() + "\n\n";
            }
        }
        return result;
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

            PieceMove bestMove = evaluateBestMove();
            state.totalPieces++;
            state.pieces++;

            if (!state.nonstop) {
                state.movesLeft--;
            }
            setShadowOnColoredBoard(bestMove.piece, bestMove.move);
            message.setStateIfNeeded(state, textBoard(), nextPieces.piece(), bestMove == null ? null : bestMove.move);

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

    private PieceMove evaluateBestMove() {
        MoveEquity bestMove = bestMove(board, nextPieces);

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
            bestMove = bestMove(board, nextPieces);
            if (bestMove == null) {
                throw new IllegalStateException("The starting position is occupied!");
            }
            lastBoardIdx = 0;
            lastBoards = new ArrayList<>();
        } else {
            nextPieces = nextPieces.next();
        }
        if (lastBoards.size() < numberOfLastBoards) {
            lastBoards.add(new BoardPieceMove(coloredBoard.copy(), bestMove.pieceMove));
            lastBoardIdx = (lastBoardIdx + 1) % numberOfLastBoards;
        } else {
            lastBoards.set(lastBoardIdx, new BoardPieceMove(coloredBoard.copy(), bestMove.pieceMove));
            lastBoardIdx = (lastBoardIdx + 1) % numberOfLastBoards;
        }

        return bestMove.pieceMove;
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

    private MoveEquity bestMove(Board board, NextPieces nextNextPieces) {
        return PositionEvaluator.bestMove(allValidPieceMoves, pieces, boardEvaluator, board, nextNextPieces);
    }

    public void togglePaused() {
        paused = !paused;
    }

    public void hide() {
        hide = true;
    }

    static class BoardPieceMove {
        public final ColoredBoard board;
        public final PieceMove pieceMove;

        public BoardPieceMove(ColoredBoard board, PieceMove pieceMove) {
            this.board = board;
            this.pieceMove = pieceMove;
        }

        @Override
        public String toString() {
            return " " + pieceMove + "\n" + board;
        }
    }
}

package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.TextBoard;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;

public class GameMessenger {
    public boolean hasUnreceivedData;
    private GameState gameState;
    public String[] board = new String[0];

    public GameMessenger(GameState gameState) {
        this.gameState = gameState;
        hasUnreceivedData = true;
    }
    
    public void setStateIfNeeded(GameState state, TextBoard textBoard, Piece piece, Move move) {
        if (!hasUnreceivedData) {
            state.duration = state.duration.stop();
            this.gameState = state.copy();
            board = textBoard.asStringRows(piece, move);
            hasUnreceivedData = true;
        }
    }

    public GameState getGameState() {
        hasUnreceivedData = false;
        return gameState;
    }
}

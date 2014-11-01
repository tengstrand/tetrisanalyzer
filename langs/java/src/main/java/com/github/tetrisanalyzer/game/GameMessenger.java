package com.github.tetrisanalyzer.game;

public class GameMessenger {
    public boolean hasUnreceivedData;
    private GameState gameState;

    public GameMessenger(GameState gameState) {
        this.gameState = gameState;
        hasUnreceivedData = true;
    }
    
    public void setStateIfNeeded(GameState gameState) {
        if (!hasUnreceivedData) {
            gameState.duration = gameState.duration.stop();
            this.gameState = gameState.copy();
            hasUnreceivedData = true;
        }
    }
    
    public GameState getGameState() {
        hasUnreceivedData = false;
        return gameState;
    }
}

package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.piece.Piece;

/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
public interface GameSettings {
    public boolean isSlidingEnabled();
    public RotationDirection rotationDirection();
    public Move pieceStartMove(int boardWidth, Piece piece);
}

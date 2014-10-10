package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.piece.Piece;

/**
 * The default settings used by the Tetris engine.
 */
public class DefaultGameSettings implements GameSettings {

    public boolean isSlidingEnabled() {
        return false;
    }

    public RotationDirection rotationDirection() {
        return new AnticlockwiseRotation();
    }

    public Move pieceStartMove(int boardWidth, Piece piece) {
        int x = (boardWidth <= 4) ? 0 : (boardWidth - 2) / 2;
        return new Move(0, x + piece.dx(0), 0 + piece.dy(0));
    }
}

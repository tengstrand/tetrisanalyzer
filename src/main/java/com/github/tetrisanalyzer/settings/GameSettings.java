package nu.tengstrand.tetrisanalyzer.settings;

import nu.tengstrand.tetrisanalyzer.move.Move;
import nu.tengstrand.tetrisanalyzer.move.rotation.RotationDirection;
import nu.tengstrand.tetrisanalyzer.piece.Piece;

/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
public interface GameSettings {
    public boolean isSlidingEnabled();
    public RotationDirection rotationDirection();
    public Move pieceStartMove(int boardWidth, Piece piece);
}

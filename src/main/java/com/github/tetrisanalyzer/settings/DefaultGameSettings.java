package nu.tengstrand.tetrisanalyzer.settings;

import nu.tengstrand.tetrisanalyzer.move.Move;
import nu.tengstrand.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import nu.tengstrand.tetrisanalyzer.move.rotation.RotationDirection;
import nu.tengstrand.tetrisanalyzer.piece.Piece;

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
        return new Move(0, (boardWidth <= 4) ? 0 : (boardWidth - 2) / 2, 0);
    }
}

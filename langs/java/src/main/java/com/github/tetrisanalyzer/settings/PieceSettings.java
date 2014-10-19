package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

public interface PieceSettings {
    RotationDirection rotationDirection();
    boolean isSlidingEnabled();
    int pieceStartX();
    Adjustments[] pieceAdjustments();
}

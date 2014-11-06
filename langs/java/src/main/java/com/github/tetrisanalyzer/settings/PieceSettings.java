package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.ClockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

public class PieceSettings {
    public final RotationDirection rotationDirection;
    public final boolean isSlidingOn;
    public final int pieceStartX;
    public final int pieceStartY;
    public final Adjustments[] pieceAdjustments;

    public PieceSettings(boolean clockwise, boolean slidingOn, int pieceStartX, int pieceStartY, Adjustments[] pieceAdjustments) {
        this.rotationDirection = clockwise ? new ClockwiseRotation() : new AnticlockwiseRotation();
        this.isSlidingOn = slidingOn;
        this.pieceStartX = pieceStartX;
        this.pieceStartY = pieceStartY;
        this.pieceAdjustments = pieceAdjustments;
    }
}

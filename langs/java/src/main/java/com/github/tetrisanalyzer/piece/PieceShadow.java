package com.github.tetrisanalyzer.piece;

import com.github.tetrisanalyzer.settings.PieceSettings;

public class PieceShadow extends Piece {

    PieceShadow(PieceSettings settings) {
        super(settings);
    }

    @Override public byte number() { return 9; }
    @Override public char character() { return ':'; }
    @Override protected int[] widths() { return new int[0]; }
    @Override protected int[] heights() { return new int[0]; }
    @Override protected PieceShape[] shapes() { return new PieceShape[0]; }
}

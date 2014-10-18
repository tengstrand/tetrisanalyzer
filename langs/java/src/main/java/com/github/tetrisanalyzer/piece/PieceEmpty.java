package com.github.tetrisanalyzer.piece;

import com.github.tetrisanalyzer.settings.PieceSettings;

public class PieceEmpty extends Piece {

    PieceEmpty(PieceSettings settings) {
        super(settings);
    }

    @Override public byte number() { return 0; }
    @Override public char character() { return '-'; }
    @Override protected int[] widths() { return new int[0]; }
    @Override protected int[] heights() { return new int[0]; }
    @Override protected PieceShape[] shapes() { return new PieceShape[0]; }
}
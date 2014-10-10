package com.github.tetrisanalyzer.piece;

public class PieceAny extends Piece {
    @Override public byte number() { return 8; }
    @Override public char character() { return 'x'; }
    @Override protected int[] widths() { return new int[0]; }
    @Override protected int[] heights() { return new int[0]; }
    @Override protected int[] dx() { return new int[0]; }
    @Override protected int[] dy() { return new int[0]; }
    @Override protected PieceShape[] shapes() { return new PieceShape[0]; }
}

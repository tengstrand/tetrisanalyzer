package nu.tengstrand.tetrisanalyzer.piece;

public class PieceEmpty extends Piece {
    @Override public byte number() { return 0; }
    @Override public char character() { return '-'; }
    @Override protected int[] widths() { return new int[0]; }
    @Override protected int[] heights() { return new int[0]; }
    @Override protected PieceShape[] shapes() { return new PieceShape[0]; }
}
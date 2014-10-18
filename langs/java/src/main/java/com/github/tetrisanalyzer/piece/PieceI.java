package com.github.tetrisanalyzer.piece;

import com.github.tetrisanalyzer.settings.PieceSettings;

public class PieceI extends Piece {

    PieceI(PieceSettings settings) {
        super(settings);
    }

    @Override public byte number() { return 2; }
    @Override public char character() { return 'I'; }
    @Override protected int[] widths() { return new int[] { 4, 1 }; }
    @Override protected int[] heights() { return new int[] { 1, 4 }; }

    @Override protected PieceShape[] shapes() {
        return new PieceShape[] {
            new PieceShape(new Point(0,0), new Point(1,0), new Point(2,0), new Point(3,0)),
            new PieceShape(new Point(0,0), new Point(0,1), new Point(0,2), new Point(0,3))
        };
    }
}

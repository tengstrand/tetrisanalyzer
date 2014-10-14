package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.piece.Piece;

import static com.github.tetrisanalyzer.piece.Piece.*;

public abstract class AbstractGameSettings implements GameSettings {

    private final int boardWidth;
    private final int boardHeight;
    private final int pieceStartX;
    private final boolean sliding;

    public AbstractGameSettings() {
        this(10, 20, 4);
    }

    public AbstractGameSettings(boolean sliding) {
        this(10, 20, 4, sliding);
    }

    public AbstractGameSettings(int boardWidth, int boardHeight, int pieceStartX) {
        this(boardWidth, boardHeight, pieceStartX, false);
    }

    public AbstractGameSettings(int boardWidth, int boardHeight, int pieceStartX, boolean sliding) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.pieceStartX = pieceStartX;
        this.sliding = sliding;
    }

    @Override
    public String export() {
        Piece[] pieces = Piece.pieces(this);

        delta2(I);

        return "Game-settings {" +
                "\n  Name: " + name() +
                "\n  Board: " + boardWidth + " x " + boardHeight +
                "\n  Sliding: " + isSlidingEnabled() +
                "\n  Rotation: " + rotationDirection() +
                "\n  Piece start position x: " + pieceStartX() +
//                "\n  Piece start position dx: " + positionDx() +
                "\n  Delta I: " + (delta(pieceDx()[I], pieceDy()[I], pieces)) +
                "\n  Delta Z: " + (delta(pieceDx()[Z], pieceDy()[Z], pieces)) +
                "\n  Delta S: " + (delta(pieceDx()[S], pieceDy()[S], pieces)) +
                "\n  Delta J: " + (delta(pieceDx()[J], pieceDy()[J], pieces)) +
                "\n  Delta L: " + (delta(pieceDx()[L], pieceDy()[L], pieces)) +
                "\n  Delta T: " + (delta(pieceDx()[T], pieceDy()[T], pieces)) +
                "\n  Delta O: " + (delta(pieceDx()[O], pieceDy()[O], pieces));
    }
// p[I].startDx
    private String positionDx() {
        Piece[] p = Piece.pieces(this);
        return "I: " + p[I].startDx + ", " +
                "Z: " + p[Z].startDx + ", " +
                "S: " + p[S].startDx + ", " +
                "J: " + p[J].startDx + ", " +
                "L: " + p[L].startDx + ", " +
                "T: " + p[T].startDx + ", " +
                "O: " + p[O].startDx;
    }

//    Delta I: [0 [2,-1] [-2,1]]

    protected String delta2(int piece) {
        Piece[] pieces = Piece.pieces(this);
        int[] dxs = pieceDx()[piece];
        int[] dys = pieceDy()[piece];

        String result = "Delta " + pieces[piece].character() + ": [" ;

        for (int i=0; i<dxs.length; i++) {

        }
        return "";
    }

    private String delta(int[] dx, int[] dy, Piece[] pieces) {
        String result = "";
        String separator = "";

        for (int i=0; i<dx.length; i++) {
            result += separator + "(" + dx[i] + "," + dy[i] + ")";
            separator = " ";
        }
        return result;
    }

    public boolean isSlidingEnabled() {
        return sliding;
    }

    @Override
    public int pieceStartX() {
        return pieceStartX;
    }

    @Override
    public int boardWidth() {
        return boardWidth;
    }

    @Override
    public int boardHeight() {
        return boardHeight;
    }

    public RotationDirection rotationDirection() {
        return new AnticlockwiseRotation();
    }

    @Override
    public String toString() {
        return export();
    }
}
